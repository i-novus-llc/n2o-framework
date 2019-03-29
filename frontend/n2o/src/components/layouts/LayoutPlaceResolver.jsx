/**
 * Created by emamoshin on 06.10.2017.
 */
import React from 'react';
import PropTypes from 'prop-types';
import isPlainObject from 'lodash.isplainobject';
import isArray from 'lodash.isarray';

/**
 * НОС парсит все children {@link Section} внутри какого-то Layout, содержимое которых будет расположено внутри {@link Place}
 * @example
 * layoutPlaceResolver(StandardWidgetLayout)
 */
const layoutPlaceResolver = LayoutComponent => {
  class LayoutComponentHOC extends React.Component {
    constructor(props) {
      super(props);
      this.getSection = this.getSection.bind(this);
      this.getParentProps = this.getParentProps.bind(this);
      this.parseSections = this.parseSections.bind(this);
    }

    /**
     * создание контекста
     * @return {{getSection: LayoutComponentHOC.getSection, getParentProps: LayoutComponentHOC.getParentProps}}
     */
    getChildContext() {
      return {
        getSection: this.getSection,
        getParentProps: this.getParentProps,
      };
    }

    /**
     * Вызов метода парсинга секций
     */
    componentWillMount() {
      this._sections = this.parseSections(this.props.children);
    }

    componentWillReceiveProps(props) {
      this._sections = this.parseSections(props.children);
    }
    /**
     * создание контекста
     * @return {{getSection: LayoutComponentHOC.getSection, getParentProps: LayoutComponentHOC.getParentProps}}
     */
    getSection(place) {
      return this._sections[place] || false;
    }

    /**
     * Парсинг всех дочерних секций
     * @param children
     * @return {*}
     */
    parseSections(children) {
      if (isPlainObject(children)) {
        if (children.type && children.type.displayName === 'Section') {
          return { [children.props.place]: children };
        }
        return {};
      } else if (isArray(children)) {
        let sections = [];
        for (let i = 0, c = children.length; i < c; i += 1) {
          sections = Object.assign(
            {},
            sections,
            this.parseSections(children[i])
          );
        }
        return sections;
      }
      return {};
    }

    /**
     * Получение родительских проспсов (передается в контекст)
     */
    getParentProps() {
      const { children, ...props } = this.props;
      return props;
    }

    /**
     * Рендер компонента-аргумента HOC
     */
    render() {
      return <LayoutComponent {...this.props} />;
    }
  }

  LayoutComponentHOC.childContextTypes = {
    getSection: PropTypes.func.isRequired,
    getParentProps: PropTypes.func.isRequired,
  };

  return LayoutComponentHOC;
};

export default layoutPlaceResolver;
