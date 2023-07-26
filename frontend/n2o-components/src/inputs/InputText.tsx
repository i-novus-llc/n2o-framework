import React, { Component, ChangeEvent, createRef, Ref, RefObject, ReactNode } from 'react'
import classNames from 'classnames'
import isFunction from 'lodash/isFunction'

import { withRightPlaceholder } from '../helpers/withRightPlaceholder'
import { TBaseInputProps, TBaseProps } from '../types'

import { Input } from './Input'

import '../styles/controls/InputText.scss'

type InputTextProps = TBaseProps & TBaseInputProps<string> & {
    active?: boolean,
    inputRef?: Ref<HTMLInputElement>,
    length?: number,
    onClick?(): void,
    onKeyDown?(): void,
    onPaste?(): void,
    prefix?: ReactNode,
    readOnly?: boolean,
    suffix?: ReactNode
}

export class InputTextComponent extends Component<InputTextProps> {
    baseInputRef: RefObject<HTMLInputElement>

    constructor(props: InputTextProps) {
        super(props)

        this.baseInputRef = createRef()
    }

    componentDidUpdate(prevProps: InputTextProps) {
        const { active } = this.props

        if (active && active !== prevProps.active) {
            this.baseInputRef.current?.focus()
        }
    }

    onChange = (e: ChangeEvent) => {
        const { onChange } = this.props

        const target = e.target as HTMLInputElement

        if (isFunction(onChange)) {
            onChange(target.value)
        }
    }

    onBlur = (e: FocusEvent) => {
        const { onBlur } = this.props

        if (isFunction(onBlur)) {
            onBlur(e)
        }
    }

    onFocus = (e: FocusEvent) => {
        const { onFocus } = this.props

        if (isFunction(onFocus)) {
            onFocus(e)
        }
    }

    handleClickAffix = () => {
        this.baseInputRef.current?.focus()
    }

    render() {
        const {
            value,
            placeholder,
            length,
            disabled,
            inputRef,
            onPaste,
            onFocus,
            onKeyDown,
            onClick,
            autoFocus,
            className,
            style,
            prefix,
            suffix,
            readOnly,
        } = this.props

        const hasAffix = !!prefix || !!suffix

        const inputProps = {
            type: 'text',
            autoFocus,
            maxLength: length,
            value: value === null ? '' : value,
            placeholder,
            disabled,
            readOnly,
            onPaste,
            onKeyDown,
            onClick,
            onChange: this.onChange,
        }

        if (hasAffix) {
            return (
                <div
                    ref={inputRef}
                    className={classNames(
                        'form-control n2o-input-text__affix-wrapper',
                        className,
                        {
                            disabled,
                        },
                    )}
                    style={style}
                >
                    {prefix ? (
                        <span
                            className={classNames('n2o-input-text__prefix')}
                            onClick={this.handleClickAffix}
                        >
                            {prefix}
                        </span>
                    ) : null}
                    <Input
                        {...inputProps}
                        inputRef={this.baseInputRef}
                        className="n2o-input-text"
                        onFocus={this.onFocus}
                        onBlur={this.onBlur}
                    />
                    {suffix ? (
                        <span
                            className={classNames('n2o-input-text__suffix')}
                            onClick={this.handleClickAffix}
                        >
                            {suffix}
                        </span>
                    ) : null}
                </div>
            )
        }

        return (
            <Input
                {...inputProps}
                className={classNames('form-control n2o-input-text', className)}
                style={style}
                onFocus={onFocus}
                onBlur={this.onBlur}
                inputRef={this.baseInputRef}
            />
        )
    }
}

export const InputText = withRightPlaceholder(InputTextComponent)
