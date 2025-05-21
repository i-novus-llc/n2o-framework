import { type WindowType } from '../components/core/WindowType'

const { N2O_WARNING_WATCHER = false } = window as WindowType

export default (condition: boolean, message: string) => {
    if (!condition) { return }

    if (process.env.NODE_ENV !== 'production' || N2O_WARNING_WATCHER) {
        // eslint-disable-next-line no-console
        console.warn(message)
    }
}
