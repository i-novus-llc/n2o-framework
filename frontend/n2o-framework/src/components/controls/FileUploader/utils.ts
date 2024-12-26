import axios, { AxiosError, CancelToken, AxiosProgressEvent, AxiosResponse } from 'axios'
import every from 'lodash/every'

import { type FileItem, type Files } from './types'

const Size: { [key: number]: string } = {
    0: 'B',
    1: 'KB',
    2: 'MB',
}

const MESSAGE = 'Ошибка загрузки файла'

export function post(
    url: string,
    file: FormData,
    onProgress: (progressEvent: AxiosProgressEvent) => void,
    onUpload: (response: AxiosResponse) => void,
    onError: (error: Error) => void,
    cancelSource: { token: CancelToken },
): void {
    axios
        .post(url, file, {
            cancelToken: cancelSource.token,
            headers: { 'Content-Type': 'multipart/form-data' },
            onUploadProgress: onProgress,
        })
        .then((response) => { onUpload(response) })
        .catch((error: AxiosError<{ statusText?: string, status?: string }>) => {
            if (error?.response) {
                const { status } = error.response

                const statusText = error.response.data?.statusText ||
                    error.response.data?.status ||
                    error.response.statusText

                onError(new Error(`${MESSAGE}: ${statusText || status}`))

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

export function deleteFile(url: string, id: string) {
    const { deleteUrl, params = {} } = getDeleteConfig(url, id)

    axios.delete(deleteUrl, { params }) as never
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
