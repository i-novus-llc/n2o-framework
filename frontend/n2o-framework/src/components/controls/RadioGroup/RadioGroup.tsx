import React, { ChangeEvent, Component } from 'react'
import uniqueId from 'lodash/uniqueId'
import toString from 'lodash/toString'
import { isEqual } from 'lodash'
import classNames from 'classnames'
import { RadioGroup as Group } from '@i-novus/n2o-components/lib/inputs/RadioGroup'
import { TOption } from '@i-novus/n2o-components/lib/types'

import Spinner from '../../snippets/Spinner/InlineSpinner'
import withFetchData from '../withFetchData'

enum RadioGroupType {
    default = 'default',
    tabs = 'tabs',
}

type Props = {
    options: Array<TOption<string | number>>,
    valueFieldId: string,
    labelFieldId: string,
    enabledFieldId: string,
    value: TOption<string | number>,
    inline: boolean,
    onChange(arg?: TOption<string | number>): void,
    disabled: boolean,
    visible: boolean,
    style: object,
    className: string,
    size: number,
    type: RadioGroupType,
    loading: boolean,
    fetchData(arg: object): void,
}

type State = {
    groupName: string,
    options: Props['options']
}

/**
 * Wrapper для радиогруппы
 * @reactProps {array} data - данные для чекбоксов
 * @reactProps {string} valueFieldId - ключ для value в data
 * @reactProps {string} labelFieldId - ключ для label в дата
 * @reactProps {string} enabledFieldId - ключ для enabled в data
 * @reactProps {string|number} value - выбранное значение
 * @reactProps {function} onChange - вызывается при изменении значения
 * @reactProps {boolean} disabled - только для чтения
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {boolean} inline - флаг вывода в ряд
 * @reactProps {object} style - стили группы
 * @reactProps {string} className - класс группы
 * @reactProps {function} fetchData - функция для получения данных
 * @reactProps {string} queryId - queryId
 * @reactProps {number} size - размер
 * @reactProps {string} type - тип чекбокса
 */
class RadioGroup extends Component<Props, State> {
    constructor(props: Props) {
        super(props)
        this.state = {
            groupName: uniqueId('n2o-radio-group-control'),
            options: [],
        }

        const { fetchData, size, labelFieldId } = this.props

        fetchData({
            size,
            [`sorting.${labelFieldId}`]: 'ASC',
        })
    }

    componentDidUpdate(prevProps: Props) {
        const { fetchData, size, labelFieldId } = this.props

        if (!isEqual(size, prevProps.size) || !isEqual(labelFieldId, prevProps.labelFieldId)) {
            fetchData({
                size,
                [`sorting.${labelFieldId}`]: 'ASC',
            })
        }
    }

    changeHandler = (event: ChangeEvent<HTMLInputElement>) => {
        const { options, valueFieldId, onChange } = this.props
        const item = options.find(item => toString(
            item[valueFieldId as keyof TOption<string | object>],
        ) === toString(event.target.value))

        return onChange(item)
    }

    static getDerivedStateFromProps({ options, valueFieldId, labelFieldId, type }: Props) {
        return {
            options: options ? options.map(radio => ({
                ...radio,
                labelClassname: classNames({ 'n2o-radio-input-tabs': type === RadioGroupType.tabs }),
                value: radio[valueFieldId as keyof TOption<string | object>],
                label: radio[labelFieldId as keyof TOption<string | object>],
            })) : [],
        }
    }

    render() {
        const {
            type,
            loading,
            inline,
            className,
            value,
            valueFieldId,
            enabledFieldId,
            disabled,
            visible,
            style,
        } = this.props
        const { groupName, options } = this.state

        if (loading) {
            return <Spinner />
        }

        const groupValue = value?.[valueFieldId as keyof TOption<string | number>]

        const groupProps = {
            value: (typeof groupValue === 'string' || typeof groupValue === 'number') ? groupValue : '',
            onChange: this.changeHandler,
            disabled,
            visible,
            enabledFieldId,
            style,
            className: classNames(className, {
                'n2o-radio-group-tabs': type === RadioGroupType.tabs,
            }),
            inline,
            name: groupName,
            options,
        }

        return <Group {...groupProps} />
    }

    static defaultProps = {
        valueFieldId: 'id',
        labelFieldId: 'name',
        value: {},
        visible: true,
        type: RadioGroupType.default,
        onChange: () => {},
        loading: false,
    } as Props
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default withFetchData(RadioGroup as any)
