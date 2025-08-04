import { createRequire } from 'module'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { config as dotenv } from 'dotenv'
import viteSvgr from 'vite-plugin-svgr'
import path from 'path'

import { proxy } from './setupProxy'
import { CHUNK_PREFIX, ROLLUP_COMMON_MODULES, VENDORS, getModuleName } from "./vendors";

export default defineConfig(({ mode }) => {
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
            rollupOptions: {
                // backward compatibility for Windows users
                input: {
                    main: 'index.html',
                },
                output: {
                    dir: 'build',
                    entryFileNames: isProduction ? 'index-[hash].js' : 'index.js',
                    chunkFileNames: 'assets/[name]-[hash].js',
                    assetFileNames: 'assets/[name]-[hash][extname]',
                    manualChunks: (id) => {     
                        if(ROLLUP_COMMON_MODULES.some((commonModule) => id.includes(commonModule))) {
                            return 'rollup'
                        }                   
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
            // transpile all dependencies to es modules as vite can't handle commonjs module types
            commonjsOptions: {
                transformMixedEsModules: true,
                include: [/node_modules/],
                defaultIsModuleExports: (moduleId) => {
                    try {
                        const require = createRequire(import.meta.url)
                        // eslint-disable-next-line import/no-dynamic-require
                        const module = require(moduleId)

                        if (module?.default) {
                            return false
                        }

                        return 'auto'
                    } catch {
                        return 'auto'
                    }
                },
            },
        },
        plugins: [
            htmlPlugin(parsed),
            reactVirtualized(),
            react({
                include: '**/*.{js,jsx,ts,tsx}',
            }),
            viteSvgr({ exportAsDefault: true }),
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
                'moment/locale/ru',
                'moment/min/moment.min.js',
                'lodash-es/debounce',
                'lodash-es/isEqual',
                'lodash-es/isEmpty',
                'lodash-es/get',
                'lodash-es/set',
                'lodash-es/map',
                'lodash-es/reduce',
                'lodash-es/isNil',
                'lodash-es/merge'
            ],
            exclude: [
                'moment-timzone',
                'enzyme',
                'jest',
                '@testing-library'
            ],
            esbuildOptions: {
                // some libraries are not browser friendly and require node.js workspace
                define: {
                    global: 'globalThis',
                    module: 'globalThis.module',
                },
            },
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

/* Фикс бага импорта в react-virtualized на котором падает vite */
import type { Plugin } from "vite";
import fs from "fs";

const WRONG_CODE = `import { bpfrpt_proptype_WindowScroller } from "../WindowScroller.js";`;
export function reactVirtualized(): Plugin {
    return {
        name: "flat:react-virtualized",
        // Note: we cannot use the `transform` hook here
        //       because libraries are pre-bundled in vite directly,
        //       plugins aren't able to hack that step currently.
        //       so instead we manually edit the file in node_modules.
        //       all we need is to find the timing before pre-bundling.
        configResolved() {
            const file = require
                .resolve("react-virtualized")
                .replace(
                    path.join("dist", "commonjs", "index.js"),
                    path.join("dist", "es", "WindowScroller", "utils", "onScroll.js"),
                );

            const code = fs.readFileSync(file, "utf-8");
            const modified = code.replace(WRONG_CODE, "");
            fs.writeFileSync(file, modified);
        },
    };
}

