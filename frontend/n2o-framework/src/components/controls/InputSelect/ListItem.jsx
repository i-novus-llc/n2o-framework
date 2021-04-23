import React from 'react'
import PropTypes from 'prop-types'
import uniqueId from 'lodash/uniqueId'
import Badge from 'reactstrap/lib/Badge'
import DropdownItem from 'reactstrap/lib/DropdownItem'
import cx from 'classnames'

import CheckboxN2o from '../Checkbox/CheckboxN2O'
import Icon from '../../snippets/Icon/Icon'
import propsResolver from '../../../utils/propsResolver'

/**
 * Компонент содержимого элемента выпадающего списка-
 * @reactProps {string} labelFieldId - значение ключа label в данных
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {string} badgeFieldId - поле для баджей
 * @reactProps {string} badgeColorFieldId - поле для цвета баджа
 * @reactProps {boolean} isExpanded - флаг видимости попапа
 * @reactProps {string} format - формат
 * @reactProps {boolean} selected - флаг выбранного элемента
 * @reactProps {boolean} hasCheckboxes - флаг наличия чекбоксов
 * @reactProps {object} item - элемент
 * @reactProps {boolean} indeterminate - флаг промежуточного состояния чекбокса
 * @reactProps {function} handleCheckboxSelect - callback на чекбокс
 */

class ListItem extends React.Component {
    constructor(props) {
        super(props)

        this._handleCheckboxClick = this._handleCheckboxClick.bind(this)
    }

    _handleCheckboxClick(e) {
        e.stopPropagation()
        e.preventDefault()
        this.props.handleCheckboxSelect(e)
    }

    /**
   * Рендер
   */

    render() {
        const {
            hasCheckboxes,
            item,
            labelFieldId,
            imageFieldId,
            iconFieldId,
            selected,
            indeterminate,
            format,
            badgeFieldId,
            badgeColorFieldId,
            ...rest
        } = this.props

        const displayTitle = (item) => {
            if (format) {
                const resolverProps = {
                    format,
                }

                return propsResolver(resolverProps, item).format
            }

            return item[labelFieldId]
        }

        return (
            <DropdownItem
                id={uniqueId('input-select-dropdown_')}
                disabled={selected}
                {...rest}
            >
                {hasCheckboxes && (
                    <CheckboxN2o
                        inline
                        className="n2o-select-checkbox"
                        value={selected}
                        onClick={this._handleCheckboxClick}
                        style={{ marginRight: 0 }}
                        indeterminate={indeterminate}
                    />
                )}
                {imageFieldId && item[imageFieldId] && <img src={item[imageFieldId]} />}
                {iconFieldId && item[iconFieldId] && <Icon name={item[iconFieldId]} />}
                <span className="text-cropped">{displayTitle(item)}</span>
                {badgeFieldId && item[badgeColorFieldId] && (
                    <Badge color={item[badgeColorFieldId]}>{item[badgeFieldId]}</Badge>
                )}
            </DropdownItem>
        )
    }
}

ListItem.propTypes = {
    handleCheckboxSelect: PropTypes.func,
    hasCheckboxes: PropTypes.bool,
    imageFieldId: PropTypes.string,
    iconFieldId: PropTypes.string,
    labelFieldId: PropTypes.string.isRequired,
    badgeFieldId: PropTypes.string,
    badgeColorFieldId: PropTypes.string,
    item: PropTypes.object.isRequired,
    selected: PropTypes.bool,
    format: PropTypes.string,
    indeterminate: PropTypes.bool,
    isExpanded: PropTypes.bool,
}

ListItem.defaultProps = {
    hasCheckboxes: false,
    selected: false,
    indeterminate: false,
}

export default ListItem
