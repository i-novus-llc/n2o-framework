import React, { useState } from 'react'
import classNames from 'classnames'
import { Button } from 'reactstrap'

import '../styles/controls/InputPassword.scss'
import { TBaseInputProps, TBaseProps } from '../types'

import { Input } from './Input'

type InputPasswordProps = TBaseProps & TBaseInputProps<string> & {
    onKeyDown?(): void,
    onPaste?(): void,
    showPasswordBtn?: boolean
}

export const InputPassword = ({
    style,
    autoFocus,
    placeholder,
    disabled,
    onPaste,
    onFocus,
    onBlur,
    onKeyDown,
    onChange,
    value = '',
    className = '',
    showPasswordBtn = true,
}: InputPasswordProps) => {
    const [showPass, setShowPass] = useState(false)
    const onToggleShowPass = () => setShowPass(!showPass)
    const type = (showPass && showPasswordBtn) ? 'text' : 'password'

    return (
        <div className="n2o-input-password">
            <Input
                className={classNames('form-control', className)}
                type={type}
                style={style}
                autoFocus={autoFocus}
                value={value || ''}
                placeholder={placeholder}
                disabled={disabled}
                onPaste={onPaste}
                onFocus={onFocus}
                onBlur={onBlur}
                onKeyDown={onKeyDown}
                onChange={onChange}
            />
            {showPasswordBtn && (
                <Button
                    className="n2o-input-password-toggler"
                    onClick={onToggleShowPass}
                    size="sm"
                    color="link"
                >
                    <i className={classNames('fa', showPass ? 'fa-eye-slash' : 'fa-eye')} />
                </Button>
            )}
        </div>
    )
}
