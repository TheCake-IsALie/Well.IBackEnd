package org.generation.wellibackend.services;

import org.generation.wellibackend.model.entities.MotivationalPhrase;
import org.generation.wellibackend.model.entities.User;
import org.generation.wellibackend.model.repositories.PhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhraseService
{
	@Autowired
	PhraseRepository phraseRepo;

	GeminiService gemini = new GeminiService();

	public String getPhrase(){
		return gemini.getResponseGemini("#!!# dammi una frase motivazionale famosa che mi sia" +
										" d'ispirazione per cominciare al meglio la giornata #!!#");
	}

	public String getAuthor(String frase){
		return gemini.getResponseGemini("#!!# Dammi l'autore della frase motivazionale " + frase + " #!!#");
	}

	public void setPhraseAndAuthor(User user) {
		MotivationalPhrase phrase=phraseRepo.findByUser(user);
		if(phrase==null){
			phrase = new MotivationalPhrase();
			phrase.setPhrase(getPhrase());
			phrase.setAuthor(getAuthor(phrase.getPhrase()));
			phrase.setUser(user);

			phraseRepo.save(phrase);
		} else
			System.out.println("\n --> È già presente in memoria una frase motivazionale per l'utente loggato.\n");
	}

	public MotivationalPhrase getPhraseAndAuthor(User user){
		return phraseRepo.findByUser(user);
	}

}
