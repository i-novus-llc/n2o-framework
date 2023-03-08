import axios from 'axios'
import every from 'lodash/every'

const Size = {
    0: 'B',
    1: 'KB',
    2: 'MB',
}

export function post(url, file, onProgress, onUpload, onError, cancelSource) {
    axios
        .post(url, file, {
            cancelToken: cancelSource.token,
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            onUploadProgress: onProgress,
        })
        .then((response) => {
            onUpload(response)
        })
        .catch((error) => {
            onError(error)
        })
}

const SPECIAL_SYMBOLS = ['?', '=']

function getDeleteConfig(url, id) {
    if (url.includes('?')) {
        const { origin, pathname, search } = new URL(url)

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

export function deleteFile(url, id) {
    const { deleteUrl, params = {} } = getDeleteConfig(url, id)

    axios.delete(deleteUrl, { params })
}

export function convertSize(size, step = 0) {
    if (!size || size === 0) {
        return ' 0B'
    }
    if (size / 1024 > 1) {
        return convertSize(size / 1024, step + 1)
    }

    return `${Math.round(size)} ${Size[step]}`
}

function beforeUpload(file) {
    const isImage =
    file.type === 'image/jpeg' ||
    file.type === 'image/png' ||
    file.type === 'image/svg+xml'

    if (!isImage) {
        return false
    }

    return isImage
}

export const everyIsValid = files => every(files, file => beforeUpload(file))
