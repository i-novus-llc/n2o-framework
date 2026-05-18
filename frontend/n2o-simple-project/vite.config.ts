import { createRequire } from 'module'
import { defineConfig, UserConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { config as dotenv } from 'dotenv'
import viteSvgr from 'vite-plugin-svgr'

import { proxy } from './setupProxy'
import { CHUNK_PREFIX, VENDORS, getModuleName } from "./vendors";

export default defineConfig(({ mode }: UserConfig) => {
    const { parsed = {} } = dotenv();
    const isProduction = mode === 'production';

    return {
        base: process.env.PUBLIC_URL,
        publicDir: './public',
        build: {
            cssCodeSplit: true,
            chunkSizeWarningLimit: 1000,
            sourcemap: true,
            outDir: 'build',
            emptyOutDir: true,
            rolldownOptions: {
                // backward compatibility for Windows users
                input: {
                    main: 'index.html',
                },
                output: {
                    dir: 'build',
                    entryFileNames: isProduction ? 'index-[hash].js' : 'index.js',
                    chunkFileNames: 'assets/[name]-[hash].js',
                    assetFileNames: 'assets/[name]-[hash][extname]',
                    manualChunks: (id: string) => {
                        if (id.includes('node_modules')) {
                            const moduleName = getModuleName(id)

                            if (moduleName) {
                                for (const [vendorName, packages] of Object.entries(VENDORS)) {
                                    if (packages.some((packageName) => moduleName.startsWith(`${packageName}/`))) {
                                        return `${CHUNK_PREFIX}${vendorName}`
                                    }
                                }
                            }
                        }

                        // TODO: Спаковать н2о в отдельный файл, не задев при этом динамически загружаемые модули. Актуально для прикладных тем
                    }
                },
            },
        },
        define: {
            global: 'globalThis',
        },
        plugins: [
            htmlPlugin(parsed),
            react(),
            viteSvgr(),
        ],
        resolve: {
            // resolve tilde syntax import from node_modules
            alias: [
                { find: /^~(.*)$/, replacement: '$1' },
                // Фикс для stompjs
                {
                    find: 'stompjs',
                    replacement: 'stompjs/lib/stomp.js',
                },
            ],
        },
        optimizeDeps: {
            include: [
                'moment/dist/locale/ru',
                'moment/dist/moment.js',
            ],
        },
        server: {
            proxy,
            host: true, // allow localhost sharing
        },
        css: {
            preprocessorOptions: {
                scss: {
                    quietDeps: true
                },
            }
        },
    }
})

/* Подмена %параметров%  из .env файла, т.к. vite из коробки делает только для ключей по префиксу VITE_ */
function htmlPlugin(env: Record<string, string>) {
    return {
        name: 'html-transform',
        transformIndexHtml(html: string) {
            let result = html

            for (const [key, value] of Object.entries(env)) {
                // фикс двойных слешей при подмене параметра
                const prefix = String(value).startsWith('/') ? '/' : ''
                const suffix = String(value).endsWith('/') ? '/' : ''

                result = result.replaceAll(`${prefix}%${key}%${suffix}`, value)
            }

            return result
        },
    }
}

