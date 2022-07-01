import 'whatwg-fetch'
import RequestError from './RequestError'

function parseResponse(response) {
    return response.text().then(text => ({
        status: response.status,
        statusText: response.statusText,
        body: text,
    }))
}

function checkStatus(parseJson = true) {
    return ({ status, statusText, body }) => {
        let responseData

        try {
            if (parseJson) {
                responseData = JSON.parse(body)
            } else {
                responseData = body
            }
        } catch (e) {
            // ничего не делаем, если не JSON
        }
        if (status < 200 || status >= 300) {
            return Promise.reject(new RequestError(statusText, status, body, responseData))
        }

        return responseData
    }
}

/**
 * Обрабытываемый запрос по URL через fetch.
 * Через этот метод запросы проверяются на ошибки и преобразуют response в json.
 * @param  {string} url       Куда слать запрос
 * @param  {object} [options] Настройки, которые пробросятся для "fetch"
 * @param  {object} [settings] Дополнительные настройки не относящиеся к fetch
 *
 * @return {object} Ответ на запрос в виде JSON
 */
export default function request(url, options = {}, { parseJson } = {}) {
    return fetch(url, options)
        .then(parseResponse)
        .then(checkStatus(parseJson))
}
