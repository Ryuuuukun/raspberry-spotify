import SockJS from 'sockjs-client/dist/sockjs.min.js';
import { Client } from "@stomp/stompjs";

class WebSocketService {
    constructor() {
        this.client = null;
        this.connected = false;
        this.subscriptions = new Map();
        this.authTokenProvider = async () => null;
    }

    async connect() {
        if (this.client?.active) {
            return;
        }

        const token = await this.authTokenProvider();
        if (!token) {
            console.warn('STOMP connection aborted: User is not authenticated or token is invalid');
            return;
        }

        this.client = new Client({
            webSocketFactory: () => new SockJS(`http://localhost:8080/ws`),
            connectHeaders: {
                Authorization: `Bearer ${token}`
            },
            reconnectDelay: 2000,
            maxReconnectDelay: 30000,
            heartbeatIncoming: 10000,
            heartbeatOutgoing: 10000,
            connectionTimeout: 10000,
            onConnect: () => {
                this.connected = true;
                this.subscriptions.forEach((callback, topic) =>
                    this.subscribe(topic, callback));
            },
            onWebSocketClose: () => {
                this.connected = false
            },
            onStompError: (frame) => {
                console.error('STOMP error: ', frame.headers.message);
            },
            beforeConnect: async () => {
                const token = await this.authTokenProvider();
                if (token) {
                    this.client.connectHeaders = {
                        Authorization: `Bearer ${token}`
                    }
                }
            }
        });

        console.log('socket connect');

        this.client.activate();
    }

    async disconnect() {
        console.log('socket disconnect');

        this.connected = false;
        if (this.client) {
            await this.client.deactivate();
            this.subscriptions.clear();
            this.client = null;
        }
    }

    subscribe(topic, callback) {
        this.subscriptions.set(topic, callback);
        if (this.connected) {
            console.log('subscribe');
            return this.client.subscribe(topic,
                message => callback(message));
        }
    }
}

export const socket = new WebSocketService();
