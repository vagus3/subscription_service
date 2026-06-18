import './styles/variable.css';
import './styles/global.css';
import './styles/components/button.css';
import './styles/components/card.css';
import './styles/utilties.css';
import './styles/app.css';

document.addEventListener('click', (event) => {
  const toggle = event.target.closest('[data-toggle]');
  if (!toggle) return;

  toggle.classList.toggle('is-on');
  toggle.setAttribute('aria-pressed', toggle.classList.contains('is-on'));
});
