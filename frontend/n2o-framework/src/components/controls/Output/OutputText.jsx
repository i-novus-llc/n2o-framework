import React, { Fragment } from 'react'
import isEqual from 'lodash/isEqual'
import isNumber from 'lodash/isNumber'
import isString from 'lodash/isString'
import PropTypes from 'prop-types'
import cx from 'classnames'
import { withTranslation } from 'react-i18next'

import Text from '../../snippets/Typography/Text/Text'
import Icon from '../../snippets/Icon/Icon'

const TypesComponents = {
    icon: ({ icon }) => <Icon className="icon" name={icon} />,
    text: ({ value, format, expandable, showFullText, preLine, isOpen, t }) => (
        <div className="text">
            <Text text={value} format={format} preLine={preLine} />
            {expandable && (
                <a href="#" onClick={showFullText} className="details-label">
                    {isOpen ? t('hide') : t('details')}
                </a>
            )}
        </div>
    ),
    iconAndText: ({
        icon,
        value,
        format,
        expandable,
        showFullText,
        preLine,
        isOpen,
        t,
    }) => (
        <>
            {icon && <Icon className="icon" name={icon} />}
            <div className="text">
                <Text text={value} format={format} preLine={preLine} />
                {expandable && (
                    <a href="#" onClick={showFullText} className="details-label">
                        {isOpen ? t('hide') : t('details')}
                    </a>
                )}
            </div>
        </>
    ),
}

/**
 * Компонент OutPutText
 * @reactProps {boolean} disabled
 * @reactProps {string} className
 * @reactProps {object} style
 * @reactProps {string} type
 * @reactProps {string} textPlace
 * @reactProps {string} icon
 * @reactProps {string} value
 * @reactProps {string} format
 */
class OutPutText extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            value: props.value,
            formattedValue: this.formatValue(props.value),
            isOpen: false,
        }

        this.setRenderComponentRef = this.setRenderComponentRef.bind(this)
        this.showFullText = this.showFullText.bind(this)
        this.formatValue = this.formatValue.bind(this)
    }

    componentDidUpdate(prevProps) {
        if (!isEqual(prevProps.value, this.props.value)) {
            this.setState({
                value: this.props.value,
                formattedValue: this.formatValue(this.props.value),
            })
        }
    }

    setRenderComponentRef(ref) {
        this.renderComponent = ref
    }

    showFullText(e) {
        e.preventDefault()
        this.setState({ isOpen: !this.state.isOpen })
    }

    formatValue(value) {
        const { expandable } = this.props

        if (isNumber(expandable) && isString(value)) {
            return `${value.substr(0, expandable - 3)}...`
        }

        return value
    }

    render() {
        const {
            textPlace,
            type,
            className,
            style,
            ellipsis,
            expandable,
            ...rest
        } = this.props
        const { isOpen, value, formattedValue } = this.state
        const RenderComponent = TypesComponents[type]

        return (
            <div
                className={cx('n2o-output-text', className, textPlace, {
                    'n2o-output-text--ellipsis':
            (ellipsis || expandable === true) && !isOpen,
                    'n2o-output-text--expandable':
            (expandable && isOpen) || isNumber(expandable),
                })}
                ref={this.setRenderComponentRef}
                style={style}
            >
                <RenderComponent
                    {...rest}
                    value={isOpen ? value : formattedValue}
                    expandable={expandable}
                    showFullText={this.showFullText}
                    isOpen={isOpen}
                />
            </div>
        )
    }
}

OutPutText.propTypes = {
    /**
   * Флаг активности
   */
    disabled: PropTypes.bool,
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Стили
   */
    style: PropTypes.object,
    /**
   * Тип значения
   */
    type: PropTypes.oneOf(Object.keys(TypesComponents)),
    /**
   * Располложение текста
   */
    textPlace: PropTypes.oneOf(['right', 'left']),
    /**
   * Иконка
   */
    icon: PropTypes.string,
    /**
   * Значение
   */
    value: PropTypes.string,
    /**
   * Формт
   */
    format: PropTypes.string,
    /**
   * Троеточие при длинном текста
   */
    ellipsis: PropTypes.bool,
    /**
   * "Подробнее" в конце строки при длинном тексте
   */
    expandable: PropTypes.oneOf([PropTypes.bool, PropTypes.number]),
}

OutPutText.defaultProps = {
    disabled: true,
    className: 'n2o',
    style: {},
    type: 'iconAndText',
    textPlace: 'left',
    ellipsis: false,
    expandable: false,
}

export default withTranslation()(OutPutText)
