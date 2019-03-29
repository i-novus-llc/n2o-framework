import React, { Component, Children } from 'react';
import PropTypes from 'prop-types';
import { first, each, isObject, isArray, isString, values } from 'lodash';

import factoryConfigShape from './factoryConfigShape';
import NotFoundFactory from './NotFoundFactory';

class FactoryProvider extends Component {
  getChildContext() {
    return {
      factories: this.factories,
      getComponent: this.getComponent,
      resolveProps: this.resolveProps
    };
  }

  constructor(props, context) {
    super(props, context);
    this.factories = props.config;
    this.getComponent = this.getComponent.bind(this);
    this.resolveProps = this.resolveProps.bind(this);
  }

  getComponent(src, level) {
    if (level && this.factories[level]) {
      return this.factories[level][src];
    } else {
      let findedFactory = [];
      each(this.factories, group => {
        if (group && group[src]) {
          findedFactory.push(group[src]);
        }
      });
      return first(findedFactory);
    }
  }

  resolveProps(props, defaultComponent = NotFoundFactory, paramName = 'component') {
    let obj = {};
    if (isObject(props)) {
      Object.keys(props).forEach(key => {
        if (isObject(props[key])) {
          obj[key] = this.resolveProps(props[key], defaultComponent, paramName);
        } else if (key === 'src') {
          obj[paramName] = this.getComponent(props[key]) || defaultComponent;
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
  children: PropTypes.element.isRequired
};

FactoryProvider.childContextTypes = {
  factories: factoryConfigShape.isRequired,
  getComponent: PropTypes.func,
  resolveProps: PropTypes.func
};

export default FactoryProvider;
