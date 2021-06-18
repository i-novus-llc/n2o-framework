import React from 'react'
import PropTypes from 'prop-types'

import StandardWidget from '../StandardWidget'
import { dependency } from '../../../core/dependency'

import TreeContainer from './container/TreeContainer'

/**
 * Виджет таблица
 * @reactProps {string} containerId - id контейнера
 * @reactProps {string} pageId - id страницы
 * @reactProps {string} widgetId - id виджета
 * @reactProps {object} actions
 * @reactProps {object} tools
 * @reactProps {object} dataProvider
 * @reactProps {object} table
 * @reactProps {number} table.size
 * @reactProps {boolean} table.fetchOnInit - фетчить / не фетчить данные при инициализации
 * @reactProps {boolean} table.hasSelect
 * @reactProps {string} table.className - css класс
 * @reactProps {object} table.style - css стили
 * @reactProps {boolean} table.autoFocus
 * @reactProps {object} table.sorting
 * @reactProps {array} table.headers
 * @reactProps {array} table.cells
 */
class TreeWidget extends React.Component {
    /**
   * Замена src на компонент
   */
    getWidgetProps() {
        const {
            hasFocus,
            hasSelect,
            autoFocus,
            rowClick,
            childIcon,
            multiselect,
            showLine,
            filter,
            expandBtn,
            bulkData,
            parentFieldId,
            valueFieldId,
            labelFieldId,
            iconFieldId,
            imageFieldIdd,
            badgeFieldId,
            badgeColorFieldId,
            hasCheckboxes,
            draggable,
            childrenFieldId,
        } = this.props
        const { toolbar, actions, dataProvider, placeholder } = this.props

        return {
            toolbar,
            actions,
            hasFocus,
            hasSelect,
            autoFocus,
            placeholder,
            dataProvider,
            rowClick,
            childIcon,
            multiselect,
            showLine,
            filter,
            expandBtn,
            bulkData,
            parentFieldId,
            valueFieldId,
            labelFieldId,
            iconFieldId,
            imageFieldIdd,
            badgeFieldId,
            badgeColorFieldId,
            hasCheckboxes,
            draggable,
            childrenFieldId,
        }
    }

    render() {
        const {
            id: widgetId,
            toolbar,
            disabled,
            actions,
            fetchOnInit,
            pageId,
            className,
            style,
        } = this.props

        return (
            <StandardWidget
                disabled={disabled}
                widgetId={widgetId}
                toolbar={toolbar}
                actions={actions}
                className={className}
                style={style}
            >
                <TreeContainer
                    widgetId={widgetId}
                    pageId={pageId}
                    fetchOnInit={fetchOnInit}
                    {...this.getWidgetProps()}
                />
            </StandardWidget>
        )
    }
}

TreeWidget.defaultProps = {
    toolbar: {},
    filter: {},
}

TreeWidget.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
    containerId: PropTypes.string.isRequired,
    pageId: PropTypes.string.isRequired,
    widgetId: PropTypes.string,
    actions: PropTypes.object,
    toolbar: PropTypes.object,
    dataProvider: PropTypes.object,
    hasFocus: PropTypes.bool,
    hasSelect: PropTypes.bool,
    autoFocus: PropTypes.any,
    rowClick: PropTypes.func,
    childIcon: PropTypes.string,
    multiselect: PropTypes.bool,
    showLine: PropTypes.bool,
    filter: PropTypes.object,
    expandBtn: PropTypes.bool,
    bulkData: PropTypes.bool,
    parentFieldId: PropTypes.string,
    valueFieldId: PropTypes.string,
    labelFieldId: PropTypes.string,
    iconFieldId: PropTypes.string,
    imageFieldIdd: PropTypes.string,
    badgeFieldId: PropTypes.string,
    badgeColorFieldId: PropTypes.string,
    hasCheckboxes: PropTypes.bool,
    draggable: PropTypes.bool,
    childrenFieldId: PropTypes.string,
    placeholder: PropTypes.string,
    id: PropTypes.string,
    disabled: PropTypes.bool,
    fetchOnInit: PropTypes.bool,
}

TreeWidget.contextTypes = {
    resolveProps: PropTypes.func,
}

export default dependency(TreeWidget)
