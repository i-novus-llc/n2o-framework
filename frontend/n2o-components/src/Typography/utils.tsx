import React, { ReactNode } from 'react'

const mnemonicMap = {
    '\n': '&#10;',
    '\t': '&#9;',
    '\s': ' ',
}

export function replaceWrappingSymbol(text: string): ReactNode {
    if (!text.includes('\n')) { return text }

    const encoded: ReactNode[] = []
    const list = text.split('\n')

    list.forEach((row, index) => {
        encoded.push(row)

        if (index < list.length - 1) { encoded.push(<br />) }
    })

    return encoded
}

export function encodeAttribute(text = '') {
    let encoded = text

    for (const [key, value] of Object.values(mnemonicMap)) {
        encoded = encoded.replaceAll(key, value)
    }

    return encoded
}
