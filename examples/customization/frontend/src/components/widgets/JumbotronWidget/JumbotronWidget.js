import React from 'react';
import PropTypes from 'prop-types';
import { Jumbotron, Button } from 'reactstrap';

/**
 * Кастомный виджет JumbortonWidget
 * @reactProps {string} header - заголовок
 * @reactProps {string} title - тайл Jumborton
 * @reactProps {string} lead - описание Jumborton
 * @reactProps {string} hint
 * @reactProps {object.{
 *     {string} text
 *     {string} color
 * }} btn - параметры кнопки
 */
class JumbotronWidget extends React.Component {
    render() {
        const { header, title, lead, hint, btn } = this.props;
        return (
            <div>
                <h1>{header}</h1>
                <Jumbotron>
                    <h1 className="display-3">{title}</h1>
                    <p className="lead">{lead}</p>
                    <hr className="my-2" />
                    <p>{hint}</p>
                    <p className="lead">
                        <Button color={btn.color}>{btn.text}</Button>
                    </p>
                </Jumbotron>
            </div>
        );
    }
}

JumbotronWidget.propTypes = {
  header: PropTypes.string,
  title: PropTypes.string,
  lead: PropTypes.string,
  hint: PropTypes.string,
  btn: PropTypes.shape({
      text: PropTypes.string,
      color: PropTypes.string
  })
};

JumbotronWidget.defaultProps = {
    btn: {
        text: 'Кнопка',
    }
};

export default JumbotronWidget;