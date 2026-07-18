import React, { forwardRef, ReactNode, ComponentProps, RefObject } from 'react'
import classNames from 'classnames'

export type InputProps = Omit<ComponentProps<'input'>, 'type'> & {
    postfix?: ReactNode
    prefix?: ReactNode
    children?: ReactNode
    inputRef: RefObject<HTMLInputElement>
    value: string
}

export const Input = forwardRef<HTMLDivElement, InputProps>(
    ({ children, postfix, prefix, inputRef, className, ...rest }, ref) => {
        return (
            <div ref={ref} className={classNames('input', className)}>
                {prefix && <div className="input-prefix">{prefix}</div>}
                <div className="input-content">
                    <div className="input-tags">{children}</div>
                    <input
                        ref={inputRef}
                        className={classNames({
                            'input-multiple': children,
                            'input-single': !children,
                        })}
                        type="text"
                        {...rest}
                    />
                </div>
                {postfix && <div className="input-postfix">{postfix}</div>}
            </div>
        )
    },
)

Input.displayName = 'Input'
