import axios, { AxiosError, CancelToken, AxiosProgressEvent, AxiosResponse } from 'axios'
import every from 'lodash/every'

import { type FileItem, type Files } from './types'

const Size: { [key: number]: string } = {
    0: 'B',
    1: 'KB',
    2: 'MB',
}

export function post(
    url: string,
    file: FormData,
    onProgress: (progressEvent: AxiosProgressEvent) => void,
    onUpload: (response: AxiosResponse) => void,
    onError: (error: Error) => void,
    cancelSource: { token: CancelToken },
    responseFieldId: string,
): void {
    axios
        .post(
            url,
            file,
            {
                cancelToken: cancelSource.token,
                headers: { 'Content-Type': 'multipart/form-data' },
                onUploadProgress: onProgress,
            },
        )
        .then((response) => { onUpload(response) })
        .catch((error: AxiosError<Record<string, string>>) => {
            if (error?.response) {
                const { data } = error.response
                const errorMessage = data?.[responseFieldId]

                if (errorMessage) {
                    onError(new Error(errorMessage))

                    return
                }

                const { status, statusText } = error.response

                onError(new Error(`Ошибка загрузки файла: ${statusText || status.toString()}`))

                return
            }

            onError(error)
        })
}

const SPECIAL_SYMBOLS = ['?', '=']

function getUrlToParse(url: string) {
    if (url.startsWith('http')) { return url }

    const { origin } = window.location

    return `${origin}${url}`
}

interface DeleteConfig {
    deleteUrl: string
    params?: { [key: string]: string }
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

        await axios.delete(deleteUrl, { params })
    } catch (error) {
        if (!axios.isAxiosError(error)) {
            throw new Error(DELETE_ERROR)
        }

        if (error.response) {
            const serverMessage = error.response.data?.message

            throw new Error(serverMessage || `${DELETE_ERROR}. Код ошибки: ${error.response.status}`)
        }

        throw new Error(`${DELETE_ERROR}: ${error.message}`)
    }
}

export function convertSize(size: number, step = 0): string {
    if (!size || size === 0) {
        return '0 B'
    }
    if (size / 1024 > 1) {
        return convertSize(size / 1024, step + 1)
    }

    return `${Math.round(size)} ${Size[step]}`
}

function beforeUpload(file: FileItem): boolean {
    return file.type === 'image/jpeg' ||
        file.type === 'image/png' ||
        file.type === 'image/svg+xml'
}

export const everyIsValid = (files: Files): boolean => every(files, file => beforeUpload(file))

export function fileAccepted(file: File, accept?: string) {
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
