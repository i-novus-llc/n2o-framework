import React, { ReactNode, cloneElement, Children, isValidElement, HTMLAttributes } from 'react'

export const ROOT_CLASS_NAME_PARAM = 'rootClassName'
export const CHILD_ROOT_CLASS_NAME = 'group-children__child'

// @INFO передает ROOT_CLASS_NAME_PARAM для установки в родительский тэг компонента
export const GroupChildren = ({
    children,
    [ROOT_CLASS_NAME_PARAM]: rootClassName,
}: { children: ReactNode, rootClassName?: string }) => {
    const rootClass = rootClassName || CHILD_ROOT_CLASS_NAME

    return (
        <>
            {Children.map(children, child => (isValidElement(child)
                ? cloneElement(child, { [ROOT_CLASS_NAME_PARAM]: rootClass } as HTMLAttributes<HTMLElement>)
                : child))}
        </>
    )
}
