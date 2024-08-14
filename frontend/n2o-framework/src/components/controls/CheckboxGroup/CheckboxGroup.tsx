import React, { ChangeEvent, ReactNode } from 'react'
import classNames from 'classnames'
import xorBy from 'lodash/xorBy'
import { CheckboxGroup as CheckboxGroupUIComponent } from '@i-novus/n2o-components/lib/inputs/CheckboxGroup'
import { InlineSpinner } from '@i-novus/n2o-components/lib/layouts/Spinner/InlineSpinner'

import withFetchData from '../withFetchData'

// eslint-disable-next-line @typescript-eslint/no-explicit-any
type Child = any

type Props = {
    id: string,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    value: any,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    options: any[],
    name: string,
    valueFieldId: string,
    labelFieldId: string,
    enabledFieldId: string,
    inline: boolean,
    loading: boolean,
    disabled: boolean,
    className?: string,
    style?: object,
    size?: number,
    children?: ReactNode,
    onBlur?(event: Event): void,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    onChange?(newValue: any, event?: Event): void,
    onFocus?(event: Event): void
    fetchData(arg: object): void,
}

export class CheckboxGroup extends React.Component<Props> {
    componentDidMount() {
        const { fetchData, size, labelFieldId } = this.props

        fetchData({
            size,
            [`sorting.${labelFieldId}`]: 'ASC',
        })
    }

    onChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { onChange: propOnChange, value, valueFieldId } = this.props
        const { value: newValue } = e.target

        if (propOnChange) {
            propOnChange(xorBy(value, [newValue], valueFieldId))
        }
    }

    onBlur = () => {
        const { onBlur: propOnBlur, value } = this.props

        if (propOnBlur) {
            propOnBlur(value)
        }
    }

    render() {
        const {
            id,
            value,
            name,
            loading,
            options,
            labelFieldId,
            valueFieldId,
            enabledFieldId,
            style,
            className: propClassName,
            children,
            inline,
            onFocus,
            disabled,
        } = this.props

        const isBtn = children && React.Children.map(children, (child: Child) => child.type.displayName)?.includes('CheckboxButton')

        const className = classNames('n2o-checkbox-group', propClassName, {
            [`btn-group${inline ? '' : '-vertical'}`]: isBtn,
            'btn-group-toggle': isBtn,
            'n2o-checkbox-inline': inline,
        })

        if (loading) {
            return <InlineSpinner />
        }

        return (
            <CheckboxGroupUIComponent
                id={id}
                name={name}
                inline={inline}
                value={value}
                options={options}
                labelFieldId={labelFieldId}
                valueFieldId={valueFieldId}
                enabledFieldId={enabledFieldId}
                className={className}
                style={style}
                onChange={this.onChange}
                onBlur={this.onBlur}
                onFocus={onFocus}
                disabled={disabled}
            />
        )
    }

    static defaultProps = {
        id: '',
        name: '',
        options: [],
        value: [],
        visible: true,
        loading: false,
        valueFieldId: 'id',
        labelFieldId: 'label',
        enabledFieldId: '',
        inline: false,
        onChange: () => {},
        onFocus: () => {},
        onBlur: () => {},
        fetchData: () => {},
        disabled: false,
    } as unknown as Props
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default withFetchData(CheckboxGroup as any)
