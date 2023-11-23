#import sounddevice
#import speech_recognition as sr
from deep_translator import GoogleTranslator


TARGET_LANGUAGES = {"russo": "ru-RU", "francês": "fr-FR", "inglês": "en-US", "alemão": "de-DE", "mandarim": "zh-CN", "polonês": "pl", "português": "pt-BR", "espanhol": "es-ES", "japonês": "ja", "coreano": "ko"}

WORDS = {"não":'0', "sim": '1', "talvez": '01', "tudo": '011', "certo": '00'}


def define_language(lang):
    lang = lang.lower()

    if lang in TARGET_LANGUAGES.keys():
        return TARGET_LANGUAGES[lang]

def translate_to_vib(text):

    text = GoogleTranslator(source='auto', target='pt').translate(text).lower()
    intersect = [k for k in text.split() if WORDS.get(k)]
    intersect_sorted = sorted(intersect, key=lambda x: text.split().index(x))
    translated_text = []
    print(intersect_sorted)
    if intersect_sorted:
        for k in intersect_sorted:
            translated_text.append(WORDS.get(k))
    return translated_text


def set_vib(text):
    vibs = []
    for w in translate_to_vib(text):
        vibs.append(w)

    print(vibs)
    vibs_duration = []

    for i, el in enumerate(vibs):
        if i != 0:
            vibs_duration.append(10)  # Adiciona zero entre os elementos
        for bit in el:
            value = 300 if bit == '0' else 800
            vibs_duration.append(value)

    return vibs_duration



texto = "Não certo sim"
print(set_vib(texto))

'''
while True:

    if def_lang is None:
        def_lang = define_language(recognizer, microphone)
        continue


    phrase = recognize_speech_from_mic(recognizer, microphone, def_lang)

    if phrase["transcription"] is not None:
        translated_text = GoogleTranslator(source='auto', target='pt').translate(phrase["transcription"]).lower()
        if 'sair' in translated_text:
            print("Saindo...")
            break

        intersect = [k for k in translated_text.split() if WORDS.get(k)]
        intersect_sorted = sorted(intersect, key=lambda x: translated_text.split().index(x))

        if intersect_sorted:
            for k in intersect_sorted:
                print(*WORDS.get(k))


    if phrase["error"]:
        print("ERROR: {}".format(phrase["error"]))
        pass
'''
