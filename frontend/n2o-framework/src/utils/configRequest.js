import 'whatwg-fetch'

export default function request() {
    return fetch('/data/config').then(response => response.json())
}
