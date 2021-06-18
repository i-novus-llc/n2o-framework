import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import has from 'lodash/has'
import { connect } from 'react-redux'

import {
    makeWidgetVisibleSelector,
    makeWidgetEnabledSelector,
    makeWidgetIsInitSelector,
} from '../selectors/widgets'
import { registerDependency } from '../actions/dependency'
import { registerWidget } from '../actions/widgets'

export const InitMetadataContext = React.createContext({
    metadata: {
        visible: true,
    },
})

/**
 * НОС - создает зависимость
 *
 */
export const dependency = (WrappedComponent) => {
    class UniversalDependency extends React.Component {
        constructor(props) {
            super(props)

            this.initIfNeeded(props)
        }

        initIfNeeded(props) {
            const {
                registerDependency,
                preInitWidget,
                dependency,
                isInit,
                id: widgetId,
                pageId,
                dataProvider,
                modelPrefix,
            } = props
            const hasVisibleDeps = has(this.props, 'dependency.visible')
            const defaultVisible = get(this.props, 'visible', true)

            if (!isInit) {
                preInitWidget({
                    widgetId,
                    pageId,
                    dataProvider,
                    modelPrefix,
                    isVisible: hasVisibleDeps ? false : defaultVisible,
                })
                registerDependency(dependency)
            }
        }

        render() {
            const { isVisible, isEnabled } = this.props

            const initMetadata = {
                metadata: {
                    visible: get(this.props, 'visible', true),
                    dependency: get(this.props, 'dependency'),
                },
            }

            return (
                <InitMetadataContext.Provider value={initMetadata}>
                    {isVisible ? (
                        <WrappedComponent
                            {...this.props}
                            disabled={!isEnabled}
                            visible={isVisible}
                        />
                    ) : null}
                </InitMetadataContext.Provider>
            )
        }
    }

    UniversalDependency.propTypes = {
        isInit: PropTypes.bool,
        isVisible: PropTypes.bool,
        isEnabled: PropTypes.bool,
        models: PropTypes.object,
    }

    const mapStateToProps = (state, props) => ({
        isInit: makeWidgetIsInitSelector(props.id)(state, props),
        isVisible: makeWidgetVisibleSelector(props.id)(state, props),
        isEnabled: makeWidgetEnabledSelector(props.id)(state, props),
    })

    const mapDispatchToProps = (dispatch, ownProps) => {
        const { id: widgetId } = ownProps

        return {
            registerDependency: dependency => dispatch(registerDependency(widgetId, dependency)),
            preInitWidget: options => dispatch(registerWidget(widgetId, options, true)),
        }
    }

    return connect(
        mapStateToProps,
        mapDispatchToProps,
    )(UniversalDependency)
}

export default dependency
