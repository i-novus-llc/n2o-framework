import React from 'react';
import { compose, lifecycle, withHandlers } from 'recompose';
import { connect } from 'react-redux';
import { batchActions } from 'redux-batched-actions';
import { createStructuredSelector } from 'reselect';
import get from 'lodash/get';

import { registerRegion, setActiveEntity, mapUrl } from '../../actions/regions';
import {
  makeRegionIsInitSelector,
  makeRegionActiveEntitySelector,
} from '../../selectors/regions';

const createRegionContainer = config => WrappedComponent => {
  const { listKey } = config;

  const RegionContainer = props => <WrappedComponent {...props} />;

  const mapStateToProps = createStructuredSelector({
    isInit: (state, props) => makeRegionIsInitSelector(props.id)(state),
    activeEntity: (state, props) =>
      makeRegionActiveEntitySelector(props.id)(state),
  });

  const mapDispatchToProps = dispatch => {
    return {
      dispatch,
    };
  };

  const enhance = compose(
    connect(
      mapStateToProps,
      mapDispatchToProps
    ),
    withHandlers({
      initIfNeeded: props => () => {
        const {
          dispatch,
          id,
          isInit,
          activeEntity,
          lazy,
          alwaysRefresh,
        } = props;

        dispatch(
          registerRegion(id, {
            regionId: id,
            activeEntity,
            isInit: true,
            lazy,
            alwaysRefresh,
            [listKey]: get(props, listKey, []),
          })
        );
      },
      changeActiveEntity: props => value => {
        const { dispatch, id } = props;

        dispatch(batchActions([setActiveEntity(id, value), mapUrl(value)]));
      },
    }),
    lifecycle({
      componentDidMount() {
        const { initIfNeeded } = this.props;

        initIfNeeded();
      },
    })
  );

  return enhance(RegionContainer);
};

export default createRegionContainer;
