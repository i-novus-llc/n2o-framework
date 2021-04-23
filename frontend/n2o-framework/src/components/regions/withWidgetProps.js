import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import omit from 'lodash/omit'
import get from 'lodash/get'
import reduce from 'lodash/reduce'
import isEmpty from 'lodash/isEmpty'

import { widgetsSelector } from '../../selectors/widgets'
import {
    makeModelsByPrefixSelector,
    modelsSelector,
    getModelsByDependency } from '../../selectors/models'
import { pagesSelector, makePageMetadataByIdSelector } from '../../selectors/pages'
import {
    hideWidget,
    showWidget,
    disableWidget,
    enableWidget,
    dataRequestWidget,
} from '../../actions/widgets'
import { PREFIXES } from '../../constants/models'
import { reduceFunction } from '../../sagas/widgetDependency'

/**
 * HOC для работы с данными
 * @param WrappedComponent - оборачиваемый компонент
 */

function withGetWidget(WrappedComponent) {
    class WithGetWidget extends React.Component {
        constructor(props) {
            super(props)

            this.getWidget = this.getWidget.bind(this)
            this.getWidgetProps = this.getWidgetProps.bind(this)
            this.getVisible = this.getVisible.bind(this)
        }

        getWidget(pageId, widgetId) {
            const state = this.context.store.getState()

            return get(makePageMetadataByIdSelector(pageId)(state), [
                'widgets',
                widgetId,
            ])
        }

        getVisible(pageId, widgetId) {
            const dependencies = get(
                this.props,
                `pages[${pageId}].metadata.widgets[${widgetId}].dependency.visible`,
                [],
            )

            if (isEmpty(dependencies)) {
                return true
            }

            const model = getModelsByDependency(dependencies)(
                this.context.store.getState(),
            )
            return reduce(model, reduceFunction, true)
        }

        getWidgetProps(widgetId) {
            return {
                ...get(this.props.widgets, widgetId, {}),
                datasource: this.props.widgetsDatasource[widgetId],
            }
        }

        /**
     * Рендер
     */
        render() {
            const props = omit(this.props, ['widgets'])

            return (
                <WrappedComponent
                    {...props}
                    getWidget={this.getWidget}
                    getWidgetProps={this.getWidgetProps}
                    getVisible={this.getVisible}
                />
            )
        }
    }

    WithGetWidget.propTypes = {
        pages: PropTypes.object,
        widgets: PropTypes.object,
        hideWidget: PropTypes.func,
        showWidget: PropTypes.func,
        disableWidget: PropTypes.func,
        enableWidget: PropTypes.func,
    }

    WithGetWidget.contextTypes = {
        store: PropTypes.object,
    }

    const mapStateToProps = state => ({
        pages: pagesSelector(state),
        widgets: widgetsSelector(state),
        widgetsDatasource: makeModelsByPrefixSelector(PREFIXES.datasource)(state),
        models: modelsSelector(state),
    })

    const mapDispatchToProps = dispatch => bindActionCreators(
        {
            hideWidget: widgetId => hideWidget(widgetId),
            showWidget: widgetId => showWidget(widgetId),
            disableWidget: widgetId => disableWidget(widgetId),
            enableWidget: widgetId => enableWidget(widgetId),
            fetchWidget: (widgetId, options) => dataRequestWidget(widgetId, options),
        },
        dispatch,
    )

    return connect(
        mapStateToProps,
        mapDispatchToProps,
    )(WithGetWidget)
}

export default withGetWidget
