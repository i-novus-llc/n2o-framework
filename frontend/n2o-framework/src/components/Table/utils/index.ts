/* eslint-disable @typescript-eslint/no-explicit-any */
// В примерах ниже используются циклы while исключительно для ускорения работы скриптов

export const excludeItems = <T>(originalArray: T[], excludeArray: T[]): T[] => {
    const excludeSet = new Set(excludeArray)
    const resultArray = []
    let i = 0

    while (i < originalArray.length) {
        const currentItem = originalArray[i]

        if (!excludeSet.has(currentItem)) {
            resultArray.push(currentItem)
        }
        i += 1
    }

    return resultArray
}

export function getAllValuesByKey <
    T extends object,
    KeyToIterate extends keyof T,
    KeyToExtract extends keyof T,
>(data: T[], options: { keyToIterate?: KeyToIterate, keyToExtract: KeyToExtract }): Array<T[KeyToExtract]>
export function getAllValuesByKey <
    T extends object,
    KeyToIterate extends keyof T,
>(data: T[], options: { keyToIterate?: KeyToIterate }): T[]
export function getAllValuesByKey <
    T extends object,
    KeyToIterate extends keyof T,
    KeyToExtract extends keyof T,
// eslint-disable-next-line max-len
>(data: T[], options: { keyToIterate?: KeyToIterate, keyToExtract?: KeyToExtract }) {
    const result: any = []
    const stack: T[] = [...data]

    const { keyToIterate, keyToExtract } = options

    while (stack.length) {
        const node = stack.pop()

        if (!node) {
            // eslint-disable-next-line no-continue
            continue
        }

        let extractValue

        if (keyToExtract !== undefined) {
            extractValue = node[keyToExtract]
        } else {
            extractValue = node
        }

        if (extractValue !== undefined && extractValue !== null) {
            result.unshift(extractValue)
        }

        if (!keyToIterate) {
            // eslint-disable-next-line no-continue
            continue
        }

        const iterableField = node[keyToIterate]

        if (Array.isArray(iterableField)) {
            stack.push(...iterableField)
        }
    }

    return result
}

/**
 * @description Функция возвращает new Map() значения из keyToExtract, если у объекта, что содержит keyToExtract есть поле по ключу keyToSearch
 * @param {array} data
 * @param {object} options
 * @param options.keyAsHash - Значение по этому ключу будет использоваться в качестве хеша для мапы
 * @param [options.keyToIterate] - Опциональный ключ по которому будет совершаться рекурсивный обход.
 */
export const getHashMapFromData = <
    T,
    KeyToIterate extends keyof T,
    KeyAsHash extends keyof T,
>(data: T[], options: { keyToIterate?: KeyToIterate; keyAsHash: KeyAsHash }): Map<T[KeyAsHash], T> => {
    const { keyToIterate, keyAsHash } = options
    const resultMap = new Map()

    function traverse(item: T) {
        const hash = item[keyAsHash]

        if (hash !== undefined && hash !== null) {
            resultMap.set(hash, item)
        }

        if (!keyToIterate) {
            return
        }

        const iterateItem = item[keyToIterate]

        if (Array.isArray(iterateItem)) {
            for (const nestedItem of iterateItem) {
                traverse(nestedItem)
            }
        }
    }

    for (const item of data) {
        traverse(item)
    }

    return resultMap
}

/**
 * Функция возвращает массив значения из keyToExtract, если у объекта, что содержит keyToExtract есть поле по ключу keyToSearch
 * @param {array} data
 * @param {object} options
 * @param options.keyToExtract - Значение которое будет добавлено в возвращаемый массив
 * @param options.keyToSearch - Ключ который будет искаться в объекте, если он есть, значение keyToExtract будет добавлено в возвращаемый массив
 * @param [options.keyToIterate = options.keyToSearch] - Опциональный ключ по которому будет совершаться рекурсивный обход. По умолчанию равен keyToSearch
 * @param [options.equalFunc] - Функция по результатам которой будет решено нужно ли добавить искомый элемент в возвращаемый массив
 */
export const getValueBySearchKey = <
    T extends Record<string, any>,
    KeyToExtract extends keyof T,
    KeyToSearch extends keyof T,
    KeyToIterate extends keyof T,
>(data: T[], options: {
        keyToExtract: KeyToExtract,
        keyToSearch: KeyToSearch,
        keyToIterate?: KeyToIterate,
        equalFunc?(data: T): boolean
    }) => {
    const { keyToExtract, equalFunc, keyToSearch, keyToIterate = keyToSearch } = options

    const defaultEqualFunc = (data: T) => data[keyToSearch] !== undefined
    const resolvedEqualFunc = equalFunc || defaultEqualFunc
    const result: Array<T[KeyToExtract]> = []

    const traverse = (items: T[]) => {
        for (const item of items) {
            const isNeedAddValue = resolvedEqualFunc(item)

            if (isNeedAddValue) {
                result.push(item[keyToExtract])
            }

            if (Array.isArray(item[keyToIterate])) {
                traverse(item[keyToIterate])
            }
        }
    }

    traverse(data)

    return result
}
