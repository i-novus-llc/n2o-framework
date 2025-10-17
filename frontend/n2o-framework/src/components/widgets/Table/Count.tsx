import React from 'react'

export interface CountProps {
    count?: string | number | null
    visible: boolean
    showCountButton: boolean
    onClick?(): void
}

export function Count({ onClick, count, visible, showCountButton }: CountProps) {
    if (!visible) { return null }

    if (count) { return <section className="pagination__total__text">{`Всего записей: ${count}`}</section> }

    if (!showCountButton) { return null }

    return (
        <button type="button" onClick={onClick} className="pagination__total">
            <span className="title">Узнать количество записей</span>
        </button>
    )
}
