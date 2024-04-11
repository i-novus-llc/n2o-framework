import * as process from 'process'
import { loadEnv } from 'vite'

export const proxy = {
    '/n2o': {
        target: loadEnv('development', process.cwd()).VITE_PROXY_TARGET || 'https://next.n2oapp.net/sandbox/view/qhkq5/',
        changeOrigin: true,
    },
}
