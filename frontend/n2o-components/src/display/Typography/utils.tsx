import React, { FC, ReactNode } from 'react'

export const delay = (ms: number) => new Promise((res) => { setTimeout(res, ms) })

type WrapTagsEntries = {
    [key: string]: (component: FC) => ({ children }: { children: ReactNode }) => ReactNode
}

export const wrapTags: WrapTagsEntries = {
    code: Comp => ({ children }) => (
        <code>
            <Comp>{children}</Comp>
        </code>
    ),
    del: Comp => ({ children }) => (
        <del>
            <Comp>{children}</Comp>
        </del>
    ),
    mark: Comp => ({ children }) => (
        <mark>
            <Comp>{children}</Comp>
        </mark>
    ),
    strong: Comp => ({ children }) => (
        <strong>
            <Comp>{children}</Comp>
        </strong>
    ),
    underline: Comp => ({ children }) => (
        <u>
            <Comp>{children}</Comp>
        </u>
    ),
    small: Comp => ({ children }) => (
        <small>
            <Comp>{children}</Comp>
        </small>
    ),
}

export const ICON_STYLE = {
    fontSize: 'initial',
}

export const moveCursorToEnd = (el: HTMLDivElement | null) => {
    if (el?.innerText && document.createRange) {
        const selection = document.getSelection()
        const range = document.createRange()

        range.setStart(el.childNodes[0], el.innerText.length)
        range.collapse(true)
        selection?.removeAllRanges()
        selection?.addRange(range)
    }
}
