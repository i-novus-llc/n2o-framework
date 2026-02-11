import { ResponseStatus } from '../constants/ResponseStatus'

import RequestError from './RequestError'

const CSRF_TOKEN_NAME = 'XSRF-TOKEN'
const CSRF_HEADER_NAME = 'X-XSRF-TOKEN'
const UNSAFE_METHODS = ['POST', 'PUT', 'DELETE', 'PATCH']

function parseCookie(cookies: string, name: string) {
    const cookie = cookies.split(';')
        .find(cookie => cookie.trim().startsWith(`${name}=`))

    return cookie ? cookie.split('=')[1].trim() : null
}

function addCsrfToken(token: string | null, options: RequestInit = {}) {
    const method = options.method ? options.method.toUpperCase() : 'GET'

    // CSRF токен нужен только для изменяющих методов
    if (token && UNSAFE_METHODS.includes(method)) {
        return {
            ...options,
            headers: {
                ...options.headers,
                [CSRF_HEADER_NAME]: token,
            },
        }
    }

    return options
}

async function checkStatus(response: Response) {
    const { status, statusText, headers } = response

    if (status === ResponseStatus.OpaqueRedirect) {
        window.location.reload()

        throw new Error('Redirect detected, page reloading')
    }

    if (status < ResponseStatus.OK || status >= ResponseStatus.MultipleChoices) {
        let json = null
        const text = await response.text()

        try { json = JSON.parse(text) } catch (e) { /* ignore */ }

        return Promise.reject(new RequestError(statusText, status, headers, text, json))
    }

    return response
}

function parseResponse(parseJson = true) {
    return async function parse(response: Response) {
        if (parseJson) {
            try {
                return await response.json()
            } catch (e) { /* ничего не делаем, если не JSON */ }
        }

        return response.text()
    }
}

function fetchWithToken(url: string, options: RequestInit = {}, token: string | null = null) {
    const extOptions: RequestInit = { ...options, redirect: 'manual' }
    const optionsWithCsrf = addCsrfToken(token, extOptions)

    return fetch(url, optionsWithCsrf)
}

function retryOnUpdateToken(url: string, options: RequestInit = {}, token: string | null = null) {
    return ((response: Response) => {
        if (response.status === 403 && !token) {
            const newToken = parseCookie(document?.cookie ?? '', CSRF_TOKEN_NAME)

            if (newToken) { return fetchWithToken(url, options, newToken) }
        }

        return response
    })
}

type ParseParam = {
    parseJson?: boolean
}

export default function request(url: string, options: RequestInit = {}, { parseJson }: ParseParam = {}) {
    const token = parseCookie(document?.cookie ?? '', CSRF_TOKEN_NAME)

    return fetchWithToken(url, options, token)
        .then(retryOnUpdateToken(url, options, token))
        .then(checkStatus)
        .then(parseResponse(parseJson))
}
