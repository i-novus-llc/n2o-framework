import React, { PureComponent } from 'react'
import { connect } from 'react-redux'
import { compose, withProps, pure } from 'recompose'

import { makePageToolbarByIdSelector } from '../../ducks/pages/selectors'

const withActions = (Component) => {
    class ComponentWithActions extends PureComponent {
        render() {
            return <Component {...this.props} />
        }
    }

    const mapStateToProps = (state, { pageId }) => ({
        entityKey: pageId,
        toolbar: makePageToolbarByIdSelector(pageId)(state),
    })

    return compose(
        withProps(({ pageId, pageUrl }) => ({
            pageId: !pageId && pageUrl ? pageUrl.substr(1) : pageId,
        })),
        pure,
        connect(mapStateToProps),
    )(ComponentWithActions)
}

export default withActions
