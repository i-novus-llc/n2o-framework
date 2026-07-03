import { ComponentType, CSSProperties } from 'react'
import { ColumnProps } from 'reactstrap/es/Col'

import { Mapping } from '../../../ducks/datasource/Provider'
import { FormModelPrefix, ModelLink } from '../../../core/models/types'
import { Model } from '../../../ducks/models/selectors'
import { type Props as StandardWidgetProps } from '../StandardWidget'

export type ActiveModel = Exclude<Model, null>

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

export type FieldsetProps = {
    rows: RowProps[]
    name?: string | null
    type?: string
    component: FieldComponent
    activeModel?: ActiveModel
    autoFocusId?: string
    autoSubmit?: boolean
}

export type FieldSetsProps = FieldsetProps[]

export interface FieldSetColComponentProps {
    col: ColProps
    activeModel: ActiveModel
    colId: string
    rowId: string | null
    autoFocusId?: string
    labelPosition: string
    labelWidth: string
    labelAlignment: string
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
    autoFocusId?: string
    disabled?: boolean
    autoSubmit?: boolean
    activeModel: ActiveModel
    onChange?(): void
    onBlur?(): void
}

export interface FieldSetRowComponentProps extends CommonRowProps {
    rowId?: string | null
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
    rowId?: string | null
}

export interface ReduxFormProps {
    name: string
    modelLink: ModelLink
    fieldsets: FieldSetsProps
    autoFocus?(): void
    autoSubmit?: boolean
    prompt?: boolean
    style?: CSSProperties
    dirty?: boolean
    className?: string
    fields?: string[]
    needActiveModel?: boolean
}

type FormWidgetPropsEnhancer = Styling & StandardWidgetProps

export interface FormWidgetProps extends FormWidgetPropsEnhancer {
    id: string
    disabled: boolean
    datasource: string
    form: { fieldsets: FieldSetsProps, modelPrefix: FormModelPrefix, prompt?: boolean }
    loading: boolean
}
