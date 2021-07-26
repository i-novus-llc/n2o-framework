import 'whatwg-fetch'

/**
 * TODO find usages and remove
 * @deprecated
 */
export default function request() {
    return fetch('/data/config').then(response => response.json())
}
