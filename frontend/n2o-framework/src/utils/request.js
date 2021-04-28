import 'whatwg-fetch'
import RequestError from './RequestError'

function parseResponse(response) {
    return response.text().then(text => ({
        status: response.status,
        statusText: response.statusText,
        body: text,
    }))
}

function checkStatus({ status, statusText, body }) {
    let json

    try {
        json = JSON.parse(body)
    } catch (e) {
    // ничего не делаем, если не JSON
    }
    if (status < 200 || status >= 300) {
        return Promise.reject(new RequestError(statusText, status, body, json))
    }

    return json
}

/**
 * Обрабытываемый запрос по URL через fetch.
 * Через этот метод запросы проверяются на ошибки и преобразуют response в json.
 * @param  {string} url       Куда слать запрос
 * @param  {object} [options] Настройки, которые пробросятся для "fetch"
 *
 * @return {object} Ответ на запрос в виде JSON
 */
export default function request(url, options = {}) {
    return fetch(url, options)
        .then(parseResponse)
        .then(checkStatus)
}
