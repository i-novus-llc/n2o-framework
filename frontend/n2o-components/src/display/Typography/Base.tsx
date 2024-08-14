import React, {
    createElement,
    createContext,
    Component,
    ReactNode,
    SyntheticEvent,
} from 'react'
import classNames from 'classnames'
import { CopyToClipboard } from 'react-copy-to-clipboard'
import keys from 'lodash/keys'
import pick from 'lodash/pick'
import flowRight from 'lodash/flowRight'
import values from 'lodash/values'
import pickBy from 'lodash/pickBy'
import isNil from 'lodash/isNil'
import isPlainObject from 'lodash/isPlainObject'

import { TBaseProps } from '../../types'
import { parseFormatter } from '../../utils/parseFormatter'

import { delay, wrapTags, ICON_STYLE } from './utils'
import { ContentEditable } from './ContentEditable'

const PropsEnd = createContext({})

type ContextType = Partial<Pick<Props, 'text' | 'format' | 'children'>>

const EndTag = () => (
    <PropsEnd.Consumer>
        {({ text, format, children }: ContextType) => {
            if (isNil(text)) {
                return children
            }

            // @ts-ignore import from js file
            const parseText = parseFormatter(text, format)

            // если text передан как object, он попадет в render и уронит приложение
            if (isPlainObject(parseText)) {
                return children
            }

            return parseText
        }}
    </PropsEnd.Consumer>
)

type MainTagOwnProps = {
    onBlur(): void
}

type MainTagProps = MainTagOwnProps & Pick<Props, 'tag' | 'color' | 'className' | 'children'>

const MainTag = ({ tag, color, className, ...props }: MainTagProps) => createElement(tag || '', {
    className: classNames(className, {
        [`text-${color} disabled`]: color,
    }),
    ...props,
})

type Props = TBaseProps & Partial<{
    // Возможность копировать
    children: ReactNode,
    code: boolean,
    // Текст
    color: string,
    copyable: boolean,
    // Флаг отображения в виде кода
    del: boolean,
    // Редактируемое поле
    editable: boolean,
    format: string,
    mark: boolean,
    onChange(textContext: string | null): void,
    // Подчеркивание текста
    small: boolean,
    // Марк
    strong: boolean,
    // Цвет
    tag: string,
    // Флаг отображения текста маленьким
    text: string,
    // Жирный текст
    underline: boolean // Callback на изменение
}>

type State = {
    copied: boolean,
    edit: boolean
}

// Базовый компонент для наследования остальных компонентов

export class Base extends Component<Props, State> {
    constructor(props: Props) {
        super(props)
        this.state = {
            copied: false,
            edit: false,
        }

        this.copyLinkClick = this.copyLinkClick.bind(this)
        this.editLinkClick = this.editLinkClick.bind(this)
        this.editableTagOnBlur = this.editableTagOnBlur.bind(this)
        this.handleContentEditable = this.handleContentEditable.bind(this)
    }

    async copyLinkClick(e: SyntheticEvent) {
        e.preventDefault()
        this.setState({ copied: true })
        await delay(3000)
        this.setState({ copied: false })
    }

    editLinkClick(e: SyntheticEvent) {
        e.preventDefault()
        this.setState({ edit: true })
    }

    editableTagOnBlur() {
        this.setState({ edit: false })
    }

    handleContentEditable(e: SyntheticEvent) {
        const { onChange } = this.props

        if (onChange) {
            onChange(e.currentTarget.textContent)
        }
    }

    render() {
        const {
            tag,
            text,
            format,
            children,
            color,
            copyable,
            editable,
            className,
            ...rest
        } = this.props

        const { copied, edit } = this.state

        const wrapperObj = pick(wrapTags, keys(pickBy(rest)))

        const wrapperFns = values(wrapperObj)
        const Wrappers = flowRight(wrapperFns)(EndTag)

        const copyIcon = !copied ? 'fa fa-files-o' : 'fa fa-check'

        const copiableFragment = (
            <span style={ICON_STYLE}>
                <CopyToClipboard text={text || ''}>
                    <button aria-label="link-button" type="button" className="pl-2 link-button" onClick={this.copyLinkClick}>
                        <i className={copyIcon} />
                    </button>
                </CopyToClipboard>
            </span>
        )

        const editableFragment = edit ? null : (
            <span style={ICON_STYLE}>
                <button aria-label="link-button" type="button" className="pl-2 link-button" onClick={this.editLinkClick}>
                    <i className="fa fa-pencil" />
                </button>
            </span>
        )

        return (
            <MainTag
                className={className}
                tag={tag}
                onBlur={this.editableTagOnBlur}
                color={color}
            >
                <ContentEditable editable={edit} onChange={this.handleContentEditable}>
                    <PropsEnd.Provider value={{ text, format, children }}>
                        <Wrappers />
                    </PropsEnd.Provider>
                </ContentEditable>
                {copyable && copiableFragment}
                {editable && editableFragment}
            </MainTag>
        )
    }

    static defaultProps = {
        code: false,
        del: false,
        mark: false,
        strong: false,
        underline: false,
        small: false,
        text: '',
        onChange: () => {},
        color: '',
    } as Props
}
