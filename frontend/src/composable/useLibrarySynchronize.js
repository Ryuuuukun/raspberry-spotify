import { useLibraryStore } from "../stores/library";

import { socket } from '@/api/websocket.js';

export function useLibrarySynchronize() {
    const library = useLibraryStore();

    let synchronizing = false;
    let pending = false;

    let timer = undefined;
    let promise = null;
    let holder = null;

    async function init() {
        holder = socket.subscribe('/user/queue/library', function (event) {
            if (synchronizing) {
                pending = true;
            } else {
                clearTimeout(timer);
                timer = setTimeout(() => {
                    synchronize().catch(error => {
                        console.error('Library sync failed: ', error);
                    });
                }, 120);
            }
        });
    }

    function destroy() {
        clearTimeout(timer);
        holder?.unsubscribe();
    }

    function synchronize() {
        if (promise) {
            pending = true;
        } else {
            promise = runSynchronization().finally(() => {
                promise = null;
            });
        }

        return promise;
    }

    async function runSynchronization() {
        if (synchronizing) {
            pending = true;
            return;
        }

        synchronizing = true;
        try {
            do {
                pending = false;

                let pages = Object.keys(library.pages || {});
                if (pages.length === 0) {
                    pages = [0];
                }

                for (const page of pages) {
                    await library.loadPage(page);
                }
            } while (pending);
        } catch (e) {
            throw e;
        } finally {
            synchronizing = false;
        }

        if (pending) {
            synchronize();
        }
    }

    return { init, destroy };
}
