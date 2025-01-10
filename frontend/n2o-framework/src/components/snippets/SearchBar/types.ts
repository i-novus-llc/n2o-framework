export const EMPTY_MENU = [
    {
        id: 'Ничего не найдено',
        label: 'Ничего не найдено',
        href: '/',
        disabled: true,
    },
]

export enum SEARCH_TRIGGER {
    ENTER = 'ENTER',
    CHANGE = 'CHANGE',
    BUTTON = 'BUTTON',
}

export type Directions = 'right' | 'left'

export interface SearchBarLinkProps {
    label?: string
    description?: string
    icon?: string | JSX.Element
    href?: string
    linkType?: string
    disabled?: boolean
    directionIconsInPopUp?: Directions
}

export interface SearchBarPopUpListProps {
    labelFieldId?: string
    descriptionFieldId?: string
    iconFieldId?: string
    urlFieldId?: string
    menu?: Array<{
        id?: string
        disabled?: boolean
        linkType?: SearchBarLinkProps['linkType']
        separateLink?: string
    }>
    directionIconsInPopUp?: Directions
    onItemClick?(): void
}

export interface SearchBarPopUpProps extends SearchBarPopUpListProps {
    dropdownOpen: boolean
}

export type SearchBarEmptyMenuProps = Pick<SearchBarPopUpProps, 'dropdownOpen' | 'urlFieldId'>

export interface SearchBarProps extends SearchBarPopUpListProps {
    className?: string
    onFocus?(): void
    placeholder?: string
    initialValue?: string
    value?: string
    initSearchValue?: string
    onSearch(value?: string | null): void
    trigger?: string
    throttleDelay?: number
    icon?: string
    button?: { label: string, icon: string } | null
    iconClear?: boolean
}

export interface SearchBarContainerProps extends Omit<SearchBarProps, 'menu' | 'onFocus'> {
    options: SearchBarProps['menu']
    descrFieldId: string
    iconFieldId: string
    labelFieldId: string
    urlFieldId: string
    fetchData(): void
}
