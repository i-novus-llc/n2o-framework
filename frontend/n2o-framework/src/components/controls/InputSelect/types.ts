import { ChangeEvent, ReactNode, RefObject, KeyboardEvent, FocusEvent, LegacyRef } from 'react'
import { type PopUpListProps } from '@i-novus/n2o-components/lib/display/PopupList/types'
import { type TagProps } from '@i-novus/n2o-components/lib/display/Tag'
import { type InputProps } from '@i-novus/n2o-components/lib/inputs/Input/Input'

export type InputComponentProps = InputProps

export enum Filter {
    endsWith = 'endsWith',
    includes = 'includes',
    server = 'server',
    startsWith = 'startsWith',
}

export type Option = { id: string } & Record<string, unknown>
export type Options = Option[]
export type PossibleSelected = Options | Option | null | undefined
export type PossibleModel = Record<string, Options | Option>

export interface SelectPaging { page: number, value?: string }

export interface SummaryFormat {
    selectFormat?: string
    /** Форма для 2–4 элементов (используется вместе с selectFormatOne и selectFormatMany) */
    selectFormatFew?: string
    /** Форма для 5+ элементов (используется вместе с selectFormatOne и selectFormatFew) */
    selectFormatMany?: string
    /** Форма для 1 элемента (используется вместе с selectFormatFew и selectFormatMany) */
    selectFormatOne?: string
    getNoun?(count: number, one: string, few: string, many: string): string
}

export interface GetPlaceholderParams extends SummaryFormat { selected: Options }

export interface FieldIds {
    valueFieldId: keyof Pick<Option, 'id'>
    labelFieldId: string
    inputLabelFieldId: string
    iconFieldId?: string
    imageFieldId?: string
    groupFieldId?: string
    descriptionFieldId?: string
    enabledFieldId?: string
    statusFieldId?: string
    sortFieldId?: string
}

interface Events {
    onSearch(value: string): void
    onFocus(): void
    onBlur(): void
    onChange(model: Option | Options | null): void
    onKeyDown?(event: KeyboardEvent): void
    fetchData(params?: object, concat?: boolean, cacheReset?: boolean): void
}

interface BooleanProps {
    popUpFullSize?: boolean
    cleanable?: boolean
    toggleOnInputClick?: boolean
    loading: boolean
    disabled?: boolean
    resetOnBlur?: boolean
    multiSelect: boolean
    closePopupOnSelect?: boolean
    hasCheckboxes: boolean
    openOnFocus?: boolean
    readOnly?: boolean
    enableCustomTags?: boolean
}

interface Paging {
    size: number
    page: number
    count: number
}

interface TagsParams {
    maxTagCount?: number
    maxTagTextLength?: number
}

interface RequestParams {
    queryId?: string
    quickSearchParam?: string
    searchMinLength?: number
}

export interface InputSelectProps
    extends SummaryFormat, FieldIds, Events, BooleanProps, Paging, TagsParams, RequestParams {
    id: string | number
    container?: string | HTMLElement | RefObject<HTMLElement>
    options: Options
    badge?: PopUpListProps['badge']
    disabledValues: []
    filter: Filter | boolean
    value?: Option
    placeholder?: string
    format?: string
    model?: PossibleModel
    className?: string
    getSearchMinLengthHint?(): string
}

// на прямую из InputSelectProps
type BaseSelectFields =
    | 'id'
    | 'readOnly'
    | 'placeholder'
    | 'disabled'
    | 'onFocus'
    | 'className'

export interface BaseSelectProps extends Pick<InputSelectProps, BaseSelectFields> {
    value: string
    onInput(e: ChangeEvent<HTMLInputElement>): void
    onInputClick(): void
    onBlur(e: FocusEvent<HTMLInputElement>): void
    onKeyDown?(e: KeyboardEvent<HTMLInputElement>): void
    inputRef: RefObject<HTMLInputElement>
    inputComponentRef?: LegacyRef<HTMLInputElement>
    postfix: ReactNode
}

export interface UseSelectKeyboardNavigationProps {
    options: Options
    selected?: PossibleSelected
    open: boolean
    onToggle(): void
}

export interface FindOptionToAdd {
    activeValueId: string | number | null
    options: Options
    inputValue: string
    labelFieldId: string
    enabledFieldId?: string
}

export interface MultiSelectProps extends BaseSelectProps, TagsParams {
    tagsRef?: RefObject<HTMLDivElement>
    onTagRemove?(removedId: string | number): void
    tags: TagProps[]
}

export type SingleSelectProps = BaseSelectProps

// на прямую из InputSelectProps
type BaseUseSelectFields =
    | 'options'
    | 'inputLabelFieldId'
    | 'enabledFieldId'
    | 'closePopupOnSelect'
    | 'openOnFocus'
    | 'popUpFullSize'
    | 'resetOnBlur'
    | 'disabled'
    | 'quickSearchParam'
    | 'loading'
    | 'onBlur'
    | 'onSearch'
    | 'toggleOnInputClick'

export interface BaseUseSelect extends Pick<InputSelectProps, BaseUseSelectFields> {
    fetchData(paging: SelectPaging): void
    // на прямую из InputSelectProps, переименованы
    defaultValue?: InputSelectProps['value']
    propsOnKeyDown?: InputSelectProps['onKeyDown']
}

export type UseSelectProps = Omit<BaseUseSelect, 'inputLabelFieldId' | 'onBlur'> & { selected?: PossibleSelected, filterOnOpen?: boolean, labelFieldId?: string }

export interface UseMultiSelectParams extends BaseUseSelect {
    selected: Options
    onChange(value: Options): void
    enableCustomTags?: boolean
    maxTagCount?: number
    summaryFormat?: SummaryFormat
}

export interface UseSingleSelectParams extends BaseUseSelect {
    selected?: Option | null
    onChange(value: Option | null): void
    labelFieldId: string
}
