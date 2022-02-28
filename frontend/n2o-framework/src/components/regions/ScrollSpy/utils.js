import { useState } from 'react'

/**
 * Преключатель состояния с таймером обратного отключения
 * @param {number} timeout
 * @param {boolean} initialValue
 * @returns {[boolean, Function]}
 */
export const useTimeoutSwitcher = (timeout, initialValue = false) => {
    const [value, setValue] = useState(initialValue)
    const [timer, setTimer] = useState(null)
    const switchValue = () => {
        setValue(!initialValue)
        clearTimeout(timer)
        setTimer(setTimeout(() => {
            setValue(initialValue)
        }, timeout))
    }

    return [value, switchValue]
}

/**
 * Поиск самого верхнего элемента
 */
const getFirst = entries => entries.reduce((prev, current) => (prev[1].offsetTop > current[1].offsetTop
    ? current
    : prev
))

/**
 * Поиск самого нижнего элемента
 */
const getLast = entries => entries.reduce((prev, current) => (prev[1].offsetTop > current[1].offsetTop
    ? prev
    : current
))

/**
 * Поиск элементов, переекаэщих видимую область контейнера
 * @param {[top: number, middle: number, bottom: number]} viewPort
 * @param entries
 */
const getEntriesOnViewPort = (viewPort, entries) => entries.filter(([, { offsetTop, clientHeight }]) => {
    const bottom = offsetTop + clientHeight

    return (
        (offsetTop >= viewPort[0] && offsetTop <= viewPort[2]) ||
        (bottom >= viewPort[0] && bottom <= viewPort[2]) ||
        (offsetTop <= viewPort[0] && bottom >= viewPort[2])
    )
})

/**
 * Поиск элемента, переекающего центр видимой области контейнера
 * @param {[top: number, middle: number, bottom: number]} viewPort
 * @param entries
 */
const getCentral = (viewPort, entries) => entries.find(([, { offsetTop, clientHeight }]) => {
    const bottom = offsetTop + clientHeight

    return (offsetTop <= viewPort[1] && bottom >= viewPort[1])
})

/**
 * Поиск активной секции в контейнере
 * - Если скрол в крайних пложениях - возвращаем первый или последний элемент
 * - Если на view-port контейнере один элемент - возвращаем его
 * - Если два - тот, который располагается по направлению скрола
 * - Если несколько - который пересекает середину видимой области контейнера
 * @param {HTMLElement} container
 * @param {{[key: string]: HTMLElement}} sections
 * @param {number} direction Направление скрола (разница между предыдущим и текущим скролом)
 */
export const getActive = (container, sections, direction) => {
    const { scrollTop, scrollHeight, clientHeight } = container
    const viewPort = [
        scrollTop, // top
        scrollTop + (clientHeight / 2), // centre
        scrollTop + clientHeight, // bottom
    ]

    const entries = Object.entries(sections)
        .filter(entry => entry[1])

    if (scrollTop === 0) {
        return getFirst(entries)[0]
    }
    if (!Math.round(scrollHeight - viewPort[2])) {
        return getLast(entries)[0]
    }

    const sectionsOnViewPort = getEntriesOnViewPort(viewPort, entries)

    if (sectionsOnViewPort.length === 1) {
        return sectionsOnViewPort[0][0]
    }
    if (sectionsOnViewPort.length === 2) {
        const entry = direction < 0 ? getFirst(sectionsOnViewPort) : getLast(sectionsOnViewPort)

        return entry[0]
    }

    return getCentral(viewPort, sectionsOnViewPort)?.[0]
}
