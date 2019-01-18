import React from 'react';
import PropTypes from 'prop-types';
import Wireframe from 'n2o/lib/components/widgets/Wireframe/Wireframe';
import { Accordion, AccordionItem } from 'react-light-accordion';
import 'react-light-accordion/demo/css/index.css';

/**
 * Кастомный регион Accordion
 * @reactProps {object} style стили для Accordion
 * @reactProps {array.<{
 *     {string} id - id панели
 *     {string} title - тайтл панели
 *     {string} description - описание панели
 * }>} panels
 */
class RegionAccordion extends React.Component {
    createItem(panel) {
        return (
            <AccordionItem key={panel.id} title={panel.title}>
                <Wireframe
                    key={panel.id}
                    title={panel.description}
                    height={300}
                />
            </AccordionItem>
        );
    }
    render() {
        const { style, panels } = this.props;
        return (
            <div style={style}>
                <h1>Кастомный регион Accordion</h1>
                <Accordion atomic={true}>
                    {panels.map((panel) => this.createItem(panel))}
                </Accordion>
            </div>
        );
    }
}

RegionAccordion.propTypes = {
  style: PropTypes.object,
  panels: PropTypes.shape({
      id: PropTypes.string,
      title: PropTypes.string,
      description: PropTypes.string
  })
};

export default RegionAccordion;