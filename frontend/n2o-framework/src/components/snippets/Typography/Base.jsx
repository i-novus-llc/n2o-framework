import React, {
    createElement,
    Fragment,
    createContext,
    Component,
} from 'react'
import cn from 'classnames'
import { CopyToClipboard } from 'react-copy-to-clipboard'
import keys from 'lodash/keys'
import pick from 'lodash/pick'
import flowRight from 'lodash/flowRight'
import values from 'lodash/values'
import pickBy from 'lodash/pickBy'
import isNil from 'lodash/isNil'
import isPlainObject from 'lodash/isPlainObject'

import parseFormatter from '../../../utils/parseFormatter'

import { delay, wrapTags, ICON_STYLE } from './utils'
import ContentEditable from './ContentEditable'
import { propTypes, defaultProps } from './propTypes'

const PropsEnd = createContext()

const EndTag = () => (
    <PropsEnd.Consumer>
        {({ text, format, children }) => {
            if (isNil(text)) {
                return children
            }

            const parseText = parseFormatter(text, format)

            // если text передан как object, он попадет в render и уронит приложение
            if (isPlainObject(parseText)) {
                return children
            }

            return parseText
        }}
    </PropsEnd.Consumer>
)

const MainTag = ({ tag, color, className, ...props }) => createElement(tag, {
    className: cn(className, {
        [`text-${color} disabled`]: color,
    }),
    ...props,
})

/**
 * Базовый компонент для наследования остальных компонентов
 * @reactProps {boolean} code - отображать в виде кода
 * @reactProps {boolean} mark - марк
 * @reactProps {boolean} strong - отображать в виде жирным
 * @reactProps {boolean} underline - нижнее подчеркивание
 * @reactProps {boolean} small - отображать маленьким
 * @reactProps {string} text - значение
 * @reactProps {node} children - внутреннее содержимое компонента
 * @reactProps {function} onChange - callback на изменение
 * @reactProps {string} color - цвет
 * @reactProps {string} editable - редактируемое поле
 * @reactProps {string} copyable - возможность копировать
 */
class Base extends Component {
    constructor(props) {
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

    async copyLinkClick(e) {
        e.preventDefault()
        this.setState({ copied: true })
        await delay(3000)
        this.setState({ copied: false })
    }

    editLinkClick(e) {
        e.preventDefault()
        this.setState({ edit: true })
    }

    editableTagOnBlur() {
        this.setState({ edit: false })
    }

    handleContentEditable(e) {
        const { onChange } = this.props

        onChange(e.currentTarget.textContent)
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
                <CopyToClipboard text={text}>
                    <a href="#" className="pl-2" onClick={this.copyLinkClick}>
                        <i className={copyIcon} />
                    </a>
                </CopyToClipboard>
            </span>
        )

        const editableFragment = edit ? null : (
            <span style={ICON_STYLE}>
                <a href="#" className="pl-2" onClick={this.editLinkClick}>
                    <i className="fa fa-pencil" />
                </a>
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
}

Base.propTypes = propTypes

Base.defaultProps = defaultProps

export default Base
