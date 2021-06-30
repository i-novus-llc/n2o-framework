import React, { PureComponent } from 'react'
import { connect } from 'react-redux'
import { compose, withProps, pure } from 'recompose'

import {
    makePageActionsByIdSelector,
    makePageToolbarByIdSelector,
} from '../../ducks/pages/selectors'

const withActions = (Component) => {
    class ComponentWithActions extends PureComponent {
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
