import React from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { compose, withProps, pure } from 'recompose'

import { getModelSelector } from '../../selectors/models'
import {
    makePageActionsByIdSelector,
    makePageMetadataByIdSelector,
    makePageToolbarByIdSelector,
} from '../../selectors/pages'

const withActions = (Component) => {
    class ComponentWithActions extends React.Component {
        render() {
            return <Component {...this.props} />
        }
    }

    const mapStateToProps = (state, props) => ({
        entityKey: props.pageId,
        actions: makePageActionsByIdSelector(props.pageId)(state),
        toolbar: makePageToolbarByIdSelector(props.pageId)(state),
    })

    return compose(
        withProps(({ pageId, pageUrl }) => ({ pageId: !pageId && pageUrl ? pageUrl.substr(1) : pageId })),
        pure,
        connect(mapStateToProps),
    )(ComponentWithActions)
}

export default withActions
