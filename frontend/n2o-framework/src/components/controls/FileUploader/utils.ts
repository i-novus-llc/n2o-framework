import every from 'lodash/every'
import get from 'lodash/get'

import { type FileItem, type Files, type FileUploadResponseData, type UploadResponse } from './types'

const Size: { [key: number]: string } = {
    0: 'B',
    1: 'KB',
    2: 'MB',
}

export function getField<T>(data: FileUploadResponseData | FileItem, fieldId: string): T | undefined {
    return get(data, fieldId) as T | undefined
}

export function post(
    url: string,
    file: FormData,
    onProgress: (progressEvent: { loaded: number; total?: number; lengthComputable: boolean }) => void,
    onUpload: (response: { data: UploadResponse; status: number; statusText: string }) => void,
    onError: (error: Error) => void,
    cancelSource: { signal?: AbortSignal }, // замена CancelToken
    responseFieldId: string,
): void {
    const xhr = new XMLHttpRequest()

    xhr.open('POST', url)

    if (cancelSource.signal) {
        cancelSource.signal.addEventListener('abort', () => {
            xhr.abort()
            onError(new Error('Upload cancelled'))
        })
    }

    xhr.upload.addEventListener('progress', (event) => {
        if (event.lengthComputable) {
            onProgress({
                loaded: event.loaded,
                total: event.total,
                lengthComputable: event.lengthComputable,
            })
        } else {
            onProgress({
                loaded: event.loaded,
                lengthComputable: false,
            })
        }
    })

    xhr.addEventListener('load', () => {
        if (xhr.status >= 200 && xhr.status < 300) {
            let data

            try {
                data = JSON.parse(xhr.responseText)
            } catch {
                data = xhr.responseText
            }

            onUpload({
                data,
                status: xhr.status,
                statusText: xhr.statusText,
            })
        } else {
            let errorMessage = `Ошибка загрузки файла: ${xhr.statusText || xhr.status}`

            try {
                const data = JSON.parse(xhr.responseText)
                const serverMessage = data?.[responseFieldId]

                if (serverMessage) {
                    errorMessage = serverMessage
                }
            } catch {
                // nothing
            }

            onError(new Error(errorMessage))
        }
    })

    xhr.addEventListener('error', () => {
        onError(new Error('Network error'))
    })

    xhr.addEventListener('abort', () => {
        onError(new Error('Upload aborted'))
    })

    xhr.send(file)
}

const SPECIAL_SYMBOLS = ['?', '=']

function getUrlToParse(url: string) {
    if (url.startsWith('http')) { return url }
    const { origin } = window.location

    return `${origin}${url}`
}

interface DeleteConfig {
    deleteUrl: string
    params?: Record<string, string>
}

function getDeleteConfig(url: string, id: string): DeleteConfig {
    if (url.includes('?')) {
        const urlToParse = getUrlToParse(url)
        const { origin, pathname, search } = new URL(urlToParse)
        const query = search
            .split('')
            .filter(char => !SPECIAL_SYMBOLS.includes(char))
            .join('')

        return {
            deleteUrl: `${origin}${pathname}`,
            params: { [query]: id },
        }
    }
    if (url.endsWith('/')) {
        return { deleteUrl: `${url}${id}` }
    }

    return { deleteUrl: `${url}/${id}` }
}

const DELETE_ERROR = 'Не удалось удалить файл'

export async function deleteFile(url: string, id: string): Promise<void> {
    try {
        const { deleteUrl, params = {} } = getDeleteConfig(url, id)
        const queryString = new URLSearchParams(params).toString()
        const finalUrl = queryString ? `${deleteUrl}?${queryString}` : deleteUrl

        const response = await fetch(finalUrl, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })

        if (!response.ok) {
            let serverMessage = ''

            try {
                const data = await response.json()

                serverMessage = data?.message
            } catch {
                // nothing
            }
            throw new Error(serverMessage || `${DELETE_ERROR}. Код ошибки: ${response.status}`)
        }
    } catch (error) {
        if (error instanceof Error) {
            throw new Error(`${DELETE_ERROR}: ${error.message}`)
        }
        throw new Error(DELETE_ERROR)
    }
}

export function convertSize(size?: number, step = 0): string {
    if (!size || size === 0) {
        return '0 B'
    }
    if (size / 1024 > 1) {
        return convertSize(size / 1024, step + 1)
    }

    return `${Math.round(size)} ${Size[step]}`
}

function beforeUpload(file: FileItem): boolean {
    return (
        file.type === 'image/jpeg' ||
        file.type === 'image/png' ||
        file.type === 'image/svg+xml'
    )
}

export const everyIsValid = (files: Files): boolean => every(files, file => beforeUpload(file))

export function fileAccepted(file: File, accept?: string): boolean {
    if (!accept) { return true }

    const formats = accept.split(',').map(f => f.trim())
    const extension = file.name.split('.').pop()?.toLowerCase() || ''
    const type = file.type.toLowerCase()

    return formats.some((format) => {
        if (format.startsWith('.')) {
            return `.${extension}` === format.toLowerCase()
        }
        if (format.includes('/*')) {
            const [mainType] = format.split('/*')

            return type.split('/')[0] === mainType.toLowerCase()
        }

        return type === format.toLowerCase()
    })
}
