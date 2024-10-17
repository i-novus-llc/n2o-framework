import React from 'react'
import isNil from 'lodash/isNil'
import uniqueId from 'lodash/uniqueId'
import classNames from 'classnames'

import { TBaseInputProps, TBaseProps } from '../../types'
import { HelpPopover } from '../../helpers/HelpPopover'
import { Input } from '../Input'

/**
 * Альтернативный чекбокс
 * @reactProps {string|number} value - уникально определяет элемент
 * @reactProps {boolean} checked - начальное значение
 * @reactProps {boolean} disabled - только для чтения / нет
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {function} onClick - событие клика по чекбоксу,
 * @reactProps {string} label - лейбл
 * @reactProps {string} className - класс копонента CheckboxN2O
 * @reactProps {string} help - подсказка в popover
 * @reactProps {boolean} inline - в ряд
 */

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export type Props = TBaseProps & TBaseInputProps<any> & {
    checked?: boolean,
    help?: string, // Подсказка в popover
    inline?: boolean, // Флаг рендера label в одну строку с контролом
    label?: string,
    onClick?(event: React.MouseEvent<HTMLDivElement>): void,
    tabIndex?: number,
    forwardedRef?: React.RefObject<HTMLDivElement>,
    preventDefault?: boolean
}

export class Checkbox extends React.Component<Props> {
    state = { id: '' }

    componentDidMount(): void {
        this.setState({ id: uniqueId('checkbox-') })
    }

    render() {
        const { className, label, disabled, value,
            inline, checked, help, tabIndex,
            style, onClick, onFocus, onChange,
            onBlur, forwardedRef, preventDefault = false } = this.props

        const { id } = this.state

        const onClickEnhancer = (event: React.MouseEvent<HTMLDivElement>): void => {
            if (preventDefault) {
                event.preventDefault()
            }
            if (onClick) {
                onClick(event)
            }
        }

        return (
            <div
                ref={forwardedRef}
                style={style}
                className={classNames(
                    className,
                    'custom-control',
                    'custom-checkbox',
                    'n2o-checkbox',
                    { 'custom-control-inline': inline, 'd-flex': help },
                )}
                onClick={onClickEnhancer}
            >
                <Input
                    id={id}
                    className="custom-control-input"
                    disabled={disabled}
                    type="checkbox"
                    value={value}
                    checked={isNil(checked) ? !!value : checked}
                    onChange={onChange}
                    onFocus={onFocus}
                    onBlur={onBlur}
                    tabIndex={tabIndex}
                    label={label}
                />
                <label className={classNames('custom-control-label', { disabled })} htmlFor={id}>{label}</label>
                {help && <HelpPopover help={help} />}
            </div>
        )
    }
}
