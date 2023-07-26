import React from 'react'

import { InputSelectComponent as InputSelect } from '@i-novus/n2o-components/lib/inputs/InputSelect/InputSelect'
import { TOption, Filter } from '@i-novus/n2o-components/lib/inputs/InputSelect/types'
import { BadgeType } from '@i-novus/n2o-components/lib/inputs/InputSelect/PopupList'

// @ts-ignore import from js file
import propsResolver from '../../../utils/propsResolver'
import listContainer from '../listContainer'

/**
 * Контейнер для {@link InputSelect}
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {array} options - данные
 * @reactProps {string} valueFieldId - значение ключа value в данных
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {object} badge - данные для баджа
 * @reactProps {boolean} disabled - флаг неактивности
 * @reactProps {boolean} disabled - только на чтение
 * @reactProps {array} disabledValues - неактивные данные
 * @reactProps {string} filter - варианты фильтрации
 * @reactProps {string} value - текущее значение
 * @reactProps {function} onInput - callback при вводе в инпут
 * @reactProps {function} onSelect - callback при выборе значения из popup
 * @reactProps {string} placeHolder - подсказка в инпуте
 * @reactProps {boolean} resetOnBlur - фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
 * @reactProps {function} onOpen - callback на открытие попапа
 * @reactProps {function} onClose - callback на закрытие попапа
 * @reactProps {string} queryId - queryId
 * @reactProps {number} size - size
 * @reactProps {boolean} multiSelect - флаг мульти выбора
 * @reactProps {string} groupFieldId - поле для группировки
 * @reactProps {boolean} closePopupOnSelect - флаг закрытия попапа при выборе
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {string} format - формат
 * @reactProps {boolean} collapseSelected - флаг сжатия выбранных элементов
 * @reactProps {number} lengthToGroup - от скольки элементов сжимать выбранные элементы
 * @reactProps {function} fetchData
 * @reactProps {function} onSearch
 * @reactProps {boolean} openOnFocus
 */

class InputSelectContainer extends React.Component<Props, State> {
    key: Props['filterValues']

    constructor(props: Props) {
        super(props)
        this.key = props.filterValues
        this.state = {
            resetMode: false,
        }
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(nextProps: Props) {
        const resetMode = (nextProps.filterValues || []).reduce(
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            (res: any, val: any) => res || val.resetMode,
            false,
        )
        const { value } = this.props

        if (resetMode && nextProps.value === value) {
            this.key = JSON.stringify(nextProps.filterValues)
            this.setState({ resetMode: true })
        } else {
            this.setState({ resetMode: false })
        }
    }

    render() {
        const { filter, options, loading, disabled, value, format } = this.props
        const { resetMode } = this.state

        const filterType = filter === 'server' ? false : filter

        let formattedOptions = [...options]

        if (format) {
            formattedOptions = options
                .map(option => ({ ...option, formattedTitle: propsResolver({ format }, option).format }))
        }

        return (
            <InputSelect
                {...this.props}
                options={formattedOptions}
                value={!resetMode && value}
                filter={filterType}
                key={this.key}
                loading={loading}
                disabled={disabled}
            />
        )
    }

    static defaultProps = {
        /**
         * Флаг загрузки
         */
        loading: false,
        /**
         * Флаг активности
         */
        disabled: false,
        /**
         * Неактивные данные
         */
        disabledValues: [],
        /**
         * Значение
         */
        value: '',
        /**
         * Фича, при которой сбрасывается значение контрола, если оно не выбрано из popup
         */
        resetOnBlur: false,
        /**
         * Варианты фильтрации
         */
        filter: false,
        /**
         * Мульти выбор значений
         */
        multiSelect: false,
        /**
         * Флаг закрытия попапа при выборе
         */
        closePopupOnSelect: true,
        /**
         * Флаг наличия чекбоксов в селекте
         */
        hasCheckboxes: false,
        /**
         * Флаг сжатия выбранных элементов
         */
        collapseSelected: true,
        /**
         * От скольки элементов сжимать выбранные элементы
         */
        lengthToGroup: 3,
        // eslint-disable-next-line react/default-props-match-prop-types
        expandPopUp: true,
        /**
         * Ключ id в данных
         */
        // eslint-disable-next-line react/default-props-match-prop-types
        valueFieldId: 'id',
        flip: false,
        /**
         * Авто фокусировка на селекте
         */
        autoFocus: false,
        /**
         * Флаг открытия попапа при фокусе на контроле
         */
        openOnFocus: false,
        options: [],
        descriptionFieldId: '',
        enabledFieldId: '',
        statusFieldId: '',
        labelFieldId: '',
        iconFieldId: '',
        imageFieldId: '',
        groupFieldId: '',
        onInput: () => {},
        onSelect: () => {},
        onOpen: () => {},
        onClose: () => {},
        fetchData: () => {},
        onSearch: () => {},
    } as Props
}

type Props = {
    loading: boolean,
    options: TOption[],
    valueFieldId: string,
    labelFieldId: string,
    iconFieldId: string,
    imageFieldId: string,
    badge?: BadgeType,
    disabled: boolean,
    disabledValues: [],
    filter: Filter | boolean,
    value: string | number,
    onInput(): void,
    onSelect(): void,
    placeholder?: string,
    flip: boolean,
    resetOnBlur: boolean,
    onOpen(): void,
    onClose(): void,
    queryId?: string,
    size?: number,
    multiSelect: boolean,
    groupFieldId: string,
    closePopupOnSelect: boolean,
    hasCheckboxes: boolean,
    format?: string,
    collapseSelected: boolean,
    lengthToGroup: number,
    fetchData(): void,
    onSearch(): void,
    autoFocus: boolean,
    openOnFocus: boolean,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    filterValues?: any,
}

type State = {
    resetMode: boolean
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export default listContainer(InputSelectContainer as any)
