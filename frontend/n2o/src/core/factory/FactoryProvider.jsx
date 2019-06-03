import React, { Component, Children, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
  first,
  each,
  isObject,
  isArray,
  isString,
  values,
  isEmpty,
  isNil,
} from 'lodash';

import factoryConfigShape from './factoryConfigShape';
import NotFoundFactory from './NotFoundFactory';
import SecurityCheck from '../auth/SecurityCheck';

class FactoryProvider extends Component {
  getChildContext() {
    return {
      factories: this.factories,
      getComponent: this.getComponent,
      resolveProps: this.resolveProps,
    };
  }

  constructor(props, context) {
    super(props, context);
    this.factories = props.config;
    this.getComponent = this.getComponent.bind(this);
    this.resolveProps = this.resolveProps.bind(this);
    this.checkSecurityAndRender = this.checkSecurityAndRender.bind(this);
  }

  checkSecurityAndRender(component = null, config, level) {
    const { securityBlackList } = this.props;
    if (isEmpty(config) || securityBlackList.includes(level)) return component;
    return props => {
      return (
        <SecurityCheck
          config={config}
          render={({ permissions }) => {
            if (permissions) {
              return React.createElement(component, props);
            }
            return null;
          }}
        />
      );
    };
  }

  getComponent(src, level, security) {
    if (level && this.factories[level] && this.factories[level][src]) {
      return this.checkSecurityAndRender(
        this.factories[level][src],
        security,
        level
      );
    }
    let findedFactory = [];
    each(this.factories, (group, level) => {
      if (group && group[src]) {
        const comp = this.checkSecurityAndRender(group[src], security, level);
        findedFactory.push(comp);
      }
    });

    return first(findedFactory);
  }

  resolveProps(
    props,
    defaultComponent = NotFoundFactory,
    paramName = 'component'
  ) {
    let obj = {};
    if (isObject(props)) {
      Object.keys(props).forEach(key => {
        if (isObject(props[key])) {
          obj[key] = this.resolveProps(props[key], defaultComponent, paramName);
        } else if (key === 'src') {
          obj[paramName] =
            this.getComponent(props[key], null, props.security) ||
            this.checkSecurityAndRender(defaultComponent, props.security);
        } else {
          obj[key] = props[key];
        }
      });
      return isArray(props) ? values(obj) : obj;
    } else if (isString(props)) {
      return this.getComponent(props) || defaultComponent;
    }
    return props;
  }

  render() {
    return Children.only(this.props.children);
  }
}

FactoryProvider.propTypes = {
  config: factoryConfigShape.isRequired,
  securityBlackList: PropTypes.array,
  children: PropTypes.element.isRequired,
};

FactoryProvider.defaultProps = {
  securityBlackList: [],
};

FactoryProvider.childContextTypes = {
  factories: factoryConfigShape.isRequired,
  getComponent: PropTypes.func,
  resolveProps: PropTypes.func,
};

export default FactoryProvider;
