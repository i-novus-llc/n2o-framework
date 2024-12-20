import { ComponentType, CSSProperties } from 'react'
import { ColumnProps } from 'reactstrap/es/Col'

import { Mapping } from '../../../ducks/datasource/Provider'
import { ModelPrefix } from '../../../core/datasource/const'
import { Model } from '../../../ducks/models/selectors'
import { Props as StandardWidgetProps } from '../StandardWidget'
import { ValidationsKey } from '../../../core/validation/types'

export type ActiveModel = Model | Model[]

interface Availability {
    visible?: boolean
    enabled?: boolean
}

interface Styling {
    className?: string
    style?: CSSProperties
}

export interface ElementProps extends Styling {
    render?(rows: RowProps[], props?: { parentName?: string }): Element[] | JSX.Element[]
    help?: string
    label?: string
    childrenLabel?: string
    needLabel?: boolean
    needDescription?: boolean
    type?: string
    activeModel?: ActiveModel
    description?: string
}

export type FieldComponent = ComponentType<ElementProps>

export interface ControlType {
    dataProvider?: DataProviderType
}

export interface FieldType extends Availability {
    id: string
    readOnly?: boolean
    control: ControlType
}

export type Fields = FieldType[]

export type DataProviderType = { queryMapping?: Mapping }

export interface ColProps extends Styling {
    fields: Fields
    cols?: ColProps[]
    fieldsets?: FieldSetsProps
    size?: ColumnProps
}

export type RowProps = { cols: ColProps[], props: Record<string, unknown> } & Styling

export interface FieldsetProps {
    rows: RowProps[]
    name?: string | null
    type?: string
    component: FieldComponent
    activeModel?: ActiveModel
    autoFocusId?: string
    modelPrefix?: ModelPrefix
    autoSubmit?: boolean
}

export type FieldSetsProps = FieldsetProps[]

export interface FieldSetColComponentProps {
    col: ColProps
    activeModel: ActiveModel
    defaultCol: ColumnProps
    colId: string
    autoFocusId?: string
    labelPosition: string
    labelWidth: string
    labelAlignment: string
    modelPrefix: ModelPrefix
    form?: string
    parentName: string
    disabled: boolean
    autoSubmit: boolean
    onChange(): void
    onBlur(): void
    multiSetDisabled: boolean
    evalContext: Record<string, unknown>
}

interface CommonRowProps {
    name?: string | null
    form?: string
    labelPosition?: string
    labelWidth?: string
    labelAlignment?: string
    defaultCol?: ColumnProps
    autoFocusId?: string
    modelPrefix: ModelPrefix
    disabled?: boolean
    autoSubmit?: boolean
    activeModel: ActiveModel
    onChange?(): void
    onBlur?(): void
}

export interface FieldSetRowComponentProps extends CommonRowProps {
    rowId: string | number
    row: RowProps
}

type FieldsetComponentPropsEnhancer = Styling & Availability & CommonRowProps

export interface FieldsetComponentProps extends FieldsetComponentPropsEnhancer {
    parentName?: string
    label?: string
    description?: string
    type?: string
    childrenLabel?: string
    help?: string
    component: FieldComponent
    multiSetDisabled?: boolean
    propsResolver?(expression?: string | boolean, model?: ActiveModel): string | boolean
    setMultiFieldDisabled?(formName: string, fields: string[], nextEnabledField: boolean): void
    setMultiFieldVisible?(formName: string, fields: string[], nextVisibleField: boolean): void
}

export interface ReduxFormProps {
    name: string
    datasource: string
    modelPrefix: ModelPrefix
    fieldsets: FieldSetsProps
    autoFocus?(): void
    autoSubmit?: boolean
    prompt?: boolean
    style?: CSSProperties
    dirty?: boolean
    className?: string
    validationKey?: ValidationsKey
    fields?: string[]
}

type FormWidgetPropsEnhancer = Styling & StandardWidgetProps

export interface FormWidgetProps extends FormWidgetPropsEnhancer {
    id: string
    disabled: boolean
    datasource: string
    form: { fieldsets: FieldSetsProps, modelPrefix: ModelPrefix }
    loading: boolean
}
