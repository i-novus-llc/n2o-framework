import React from 'react'
import PropTypes from 'prop-types'
import 'whatwg-fetch'
import { compose } from 'recompose'
import isArray from 'lodash/isArray'

import withSecurity from '../../core/auth/withSecurity'
import { SECURITY_CHECK } from '../../core/auth/authTypes'

import SideBar from './SideBar'

/**
 * Компонент контейнер для {@link SideBar}
 */
class SidebarContainer extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
        }
    }

    componentWillReceiveProps() {
        this.getItemsWithAccess()
    }

    componentDidMount() {
        this.getItemsWithAccess()
    }

    async checkItem(item, type) {
        if (item.security) {
            const { user, authProvider } = this.props
            const config = item.security

            try {
                const permissions = await authProvider(SECURITY_CHECK, {
                    config,
                    user,
                })

                this.setState({ items: this.state.items.concat(item) })
            } catch (error) {
                // ...
            }
        } else {
            this.setState({ items: this.state.items.concat(item) })
        }
    }

    getItemsWithAccess() {
        this.setState(
            {
                items: [],
            },
            () => this.makeSecure(this.props),
        )
    }

    makeSecure(metadata) {
        const makeSecure = async (items) => {
            if (isArray(items)) {
                for (const item of items) {
                    await this.checkItem(item)
                }
            }
        }
        const { items } = metadata

        makeSecure(items)
    }

    render() {
        const { items, extraItems } = this.state

        return <SideBar {...this.props} items={items} />
    }
}

SidebarContainer.propTypes = {
    items: PropTypes.array,
}

export default compose(withSecurity)(SidebarContainer)
