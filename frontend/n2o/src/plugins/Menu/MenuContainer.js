import React from 'react';
import PropTypes from 'prop-types';
import {
  getContext,
  compose,
  mapProps,
  lifecycle,
  withHandlers,
  withState,
} from 'recompose';
import { withRouter } from 'react-router-dom';
import { isArray, isEqual, uniqBy, isEmpty } from 'lodash';
import withSecurity from '../../core/auth/withSecurity';
import { SECURITY_CHECK } from '../../core/auth/authTypes';

function MenuContainer({ render, renderProps }) {
  return render(renderProps);
}

MenuContainer.propTypes = {
  render: PropTypes.func,
};

MenuContainer.defaultProps = {
  render: () => {},
};

export default compose(
  getContext({
    getMenu: PropTypes.func,
  }),
  withRouter,
  withSecurity,
  mapProps(({ getMenu, ...rest }) => ({
    ...rest,
    ...(getMenu && {
      ...getMenu(),
    }),
  })),

  withState('menuProps', 'setMenuProps', { items: [], extraItems: [] }),

  withHandlers({
    checkItem: props => async (item, type) => {
      if (item.security) {
        const { user, authProvider } = props;
        const config = item.security;
        try {
          await authProvider(SECURITY_CHECK, {
            config,
            user,
          });
          props.setMenuProps(prevState => ({
            ...prevState,
            [type]: uniqBy(prevState[type].concat(item), 'id'),
          }));
        } catch (error) {
          //...
        }
      } else {
        props.setMenuProps(prevState => ({
          ...prevState,
          [type]: uniqBy(prevState[type].concat(item), 'id'),
        }));
      }
    },
  }),

  withHandlers({
    makeSecure: props => async metadata => {
      const makeSecure = async (items, type) => {
        if (isArray(items) && !isEmpty(items)) {
          for (const item of items) {
            await props.checkItem(item, type);
          }
        }
      };
      const { items, extraItems } = metadata;
      await makeSecure(items, 'items');
      await makeSecure(extraItems, 'extraItems');
    },
  }),

  withHandlers({
    getItemsWithAccess: props => async () => {
      await props.setMenuProps({ items: [], extraItems: [] });
      await props.makeSecure(props);
    },
  }),

  lifecycle({
    async componentDidUpdate(prevProps) {
      if (
        !isEqual(this.props.items, prevProps.items) ||
        !isEqual(this.props.extraItems, prevProps.extraItems) ||
        !isEqual(this.props.user, prevProps.user)
      ) {
        await this.props.getItemsWithAccess();
      }
    },

    async componentDidMount() {
      await this.props.getItemsWithAccess();
    },
  }),
  mapProps(({ render, menuProps, ...props }) => ({
    render,
    renderProps: { ...props, ...menuProps },
  }))
)(MenuContainer);
