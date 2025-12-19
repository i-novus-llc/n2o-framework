import { ReactNode, cloneElement, Children, isValidElement } from 'react'

export const mapWithClassName = (
    children: ReactNode,
    className: string = 'group-children__child',
) => Children.map(children, child => (
    isValidElement(child)
        ? cloneElement(child, { className: child.props.className || className })
        : child)
)
