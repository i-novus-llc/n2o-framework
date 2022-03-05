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
 * Поиск активной секции в контейнере
 * @param {HTMLElement} container
 * @param {{[key: string]: HTMLElement}} sections
 * @param {number} direction Направление скрола (разница между предыдущим и текущим скролом)
 */
export const getActive = (container, sections, offset) => {
    const { scrollTop, scrollHeight, clientHeight } = container
    const viewPort = [
        scrollTop - offset, // top
        scrollTop + (clientHeight / 2) - offset, // centre
        scrollTop + clientHeight - offset, // bottom
    ]

    const entries = Object.entries(sections)
        .filter(entry => entry[1])

    // return first
    if (scrollTop <= offset) {
        return entries.reduce((prev, current) => (prev[1].offsetTop > current[1].offsetTop
            ? current
            : prev
        ))[0]
    }

    // return last
    if (!Math.round(scrollHeight - (scrollTop + clientHeight))) {
        return entries.reduce((prev, current) => (prev[1].offsetTop > current[1].offsetTop
            ? prev
            : current
        ))[0]
    }

    // return nearest to center
    return entries.find(([, { offsetTop, clientHeight }]) => {
        const bottom = offsetTop + clientHeight

        return (offsetTop <= viewPort[1] && bottom >= viewPort[1])
    })?.[0]
}
