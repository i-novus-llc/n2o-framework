import React, { ComponentType, FC } from 'react'
import classNames from 'classnames'

import { DateTimeControlName } from '../types'

import { type WithDecoratorsProps, type DateMaskProps } from './types'

export const WithDecorators = <P extends DateMaskProps>(Component: ComponentType<P>) => {
    const Wrapper: FC<P & WithDecoratorsProps> = ({
        name,
        setVisibility,
        disabled,
        inputClassName,
        setControlRef,
        onClick,
        ...rest
    }) => {
        const prefixComponent = name === DateTimeControlName.END && <span style={{ alignSelf: 'center' }}>-</span>
        const suffixComponent = (name === DateTimeControlName.DEFAULT_NAME ||
            name === DateTimeControlName.END) && (
                <button
                    type="button"
                    aria-label="calendar-button"
                    disabled={disabled}
                    onClick={onClick}
                    className="btn n2o-calendar-button"
                    tabIndex={-1}
                >
                    <i className="fa fa-calendar" aria-hidden="true" />
                </button>
        )

        return (
            <Component
                prefixComponent={prefixComponent}
                suffixComponent={suffixComponent}
                disabled={disabled}
                onClick={onClick}
                className={classNames('n2o-date-input', {
                    'n2o-date-input-first': name === DateTimeControlName.BEGIN,
                    'n2o-date-input-last': name === DateTimeControlName.END,
                })}
                inputClassName={classNames('form-control', inputClassName)}
                style={{ flexGrow: 1 }}
                {...rest as P}
                ref={setControlRef}
            />
        )
    }

    return Wrapper
}
