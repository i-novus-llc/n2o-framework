import React from 'react'
import PropTypes from 'prop-types'
import { getContext, compose, mapProps } from 'recompose'
import isArray from 'lodash/isArray'
import isEqual from 'lodash/isEqual'
import uniqBy from 'lodash/uniqBy'
import isEmpty from 'lodash/isEmpty'
import filter from 'lodash/filter'
import get from 'lodash/get'
import toArray from 'lodash/toArray'
import findIndex from 'lodash/findIndex'
import { withRouter } from 'react-router-dom'

import withSecurity from '../../core/auth/withSecurity'
import { SECURITY_CHECK } from '../../core/auth/authTypes'

export class MenuContainer extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            items: [],
            extraItems: [],
        }

        this.mapRenderProps = this.mapRenderProps.bind(this)
        this.checkItem = this.checkItem.bind(this)
        this.makeSecure = this.makeSecure.bind(this)
        this.getItemsWithAccess = this.getItemsWithAccess.bind(this)
        this.setSubItem = this.setSubItem.bind(this)
    }

    async componentDidMount() {
        await this.getItemsWithAccess()
    }

    async componentDidUpdate(prevProps) {
        if (
            !isEqual(this.props.items, prevProps.items) ||
      !isEqual(this.props.extraItems, prevProps.extraItems) ||
      !isEqual(this.props.user, prevProps.user)
        ) {
            await this.getItemsWithAccess()
        }
    }

    async checkItem(item, type, id = null) {
        if (item.security) {
            const { user, authProvider } = this.props
            const config = item.security

            try {
                await authProvider(SECURITY_CHECK, {
                    config,
                    user,
                })

                if (!id) {
                    this.setState(prevState => ({
                        ...prevState,
                        [type]: toArray(uniqBy(prevState[type].concat(item), 'id')),
                    }))
                }
            } catch (error) {
                if (id) {
                    this.setSubItem(item, type, id)
                }
            }
        } else {
            if (!id) {
                this.setState(prevState => ({
                    ...prevState,
                    [type]: toArray(uniqBy(prevState[type].concat(item), 'id')),
                }))
            }
            if (item.subItems) {
                for (const subItem of item.subItems) {
                    await this.checkItem(subItem, 'items', item.id)
                }
            }
        }
    }

    setSubItem(item, type, id) {
        const parentIndex = findIndex(this.state[type], i => i.id === id)
        const parentItem = get(this.state[type], parentIndex.toString())
        let subItems = get(parentItem, 'subItems', [])
        subItems = filter(subItems, i => i.id !== item.id)
        parentItem.subItems = subItems
        this.setState((prevState) => {
            const newState = prevState
            newState[type][parentIndex].subItems = subItems
            return newState
        })
    }

    async makeSecure(metadata) {
        const makeSecure = async (items, type) => {
            if (isArray(items) && !isEmpty(items)) {
                for (const item of items) {
                    await this.checkItem(item, type)
                }
            }
        }
        const { items, extraItems } = metadata
        await makeSecure(items, 'items')
        await makeSecure(extraItems, 'extraItems')
    }

    async getItemsWithAccess() {
        await this.setState({ items: [], extraItems: [] })
        await this.makeSecure(this.props)
    }

    mapRenderProps() {
        return {
            ...this.props,
            items: filter(
                this.state.items,
                i => !i.subItems || !isEmpty(i.subItems),
            ),
            extraItems: filter(
                this.state.extraItems,
                i => !i.subItems || !isEmpty(i.subItems),
            ),
        }
    }

    render() {
        const { render } = this.props
        return render(this.mapRenderProps())
    }
}

MenuContainer.propTypes = {
    render: PropTypes.func,
}

MenuContainer.defaultProps = {
    render: () => {},
}

export default compose(
    getContext({
        getFromConfig: PropTypes.func,
    }),
    withRouter,
    withSecurity,
    mapProps(({ getFromConfig, ...rest }) => ({
        ...rest,
        ...(getFromConfig && {
            ...getFromConfig('menu'),
        }),
    })),
)(MenuContainer)
